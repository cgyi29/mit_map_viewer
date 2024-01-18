export class CanvasBase {
    constructor(canvasId) {
        this.canvasId = canvasId;
        this.data = data;
        this.zoom = 1;
        this.panX = 0;
        this.panY = 0;
        this.mapCanvas = document.getElementById(canvasId);
        this.context = this.mapCanvas.getContext('2d');
        this.currentCanvasId = null;
        this.dragStart = null;


        this.feature = {
            pointProp : [],
            pointGeom : [],
            polyLineProp : [],
            polyLineGeom : [],
            polygonProp : [],
            polygonGeom : [],
            pointObj: null,
            polyLineObj: null,
            polygonObj: null,
            largestBbox: { minX: Infinity, minY: Infinity, maxX: -Infinity, maxY: -Infinity }
        };
    }

    addCommonEventListeners() {
        ['pointMapCanvas', 'polyLineMapCanvas', 'polygonMapCanvas'].forEach(canvasId => {
            const canvas = document.getElementById(canvasId);
            canvas.addEventListener('wheel', this.scrollHandler.bind(this));
            canvas.addEventListener('mousedown', this.startDrag.bind(this));
            canvas.addEventListener('mousemove', this.onDrag.bind(this));
            canvas.addEventListener('mouseup', this.endDrag.bind(this));
            canvas.addEventListener('mouseleave', this.endDrag.bind(this));
        });
    }


    removeCommonEventListeners() {
        ['pointMapCanvas', 'polyLineMapCanvas', 'polygonMapCanvas'].forEach(canvasId => {
            const canvas = document.getElementById(canvasId);
            if (canvas) {
                canvas.removeEventListener('wheel', this.scrollHandler);
                canvas.removeEventListener('mousedown', this.startDrag);
                canvas.removeEventListener('mousemove', this.onDrag);
                canvas.removeEventListener('mouseup', this.endDrag);
                canvas.removeEventListener('mouseleave', this.endDrag);
            }
        });
    }

    addClickEventListener(canvasId) {
        const canvas = document.getElementById(canvasId);
        canvas.addEventListener('click', this.onClick);
    }

    updateLargestBBox(bbox) {
        this.feature.largestBbox.minX = Math.min(this.feature.largestBbox.minX, bbox.minX);
        this.feature.largestBbox.minY = Math.min(this.feature.largestBbox.minY, bbox.minY);
        this.feature.largestBbox.maxX = Math.max(this.feature.largestBbox.maxX, bbox.maxX);
        this.feature.largestBbox.maxY = Math.max(this.feature.largestBbox.maxY, bbox.maxY);
    }

    updateCanvasOrder() {
        const orderedIds = $("#sortable").sortable("toArray", { attribute: "data-canvasid" });
        orderedIds.forEach((id, index) => {
            document.getElementById(id).style.zIndex = orderedIds.length - index;
        });

        const firstCanvasId = orderedIds[0];
        this.currentCanvasId = firstCanvasId;  // 현재 캔버스 ID를 설정
        this.removeCommonEventListeners();
        this.addCommonEventListeners();
        this.addClickEventListener(firstCanvasId);
    }

    onClick(event){
        if (!this.currentCanvasId) {
            console.error('currentCanvasId가 설정되지 않았습니다.');
            return;
        }
        const mapCanvas = document.getElementById(currentCanvasId);
        const rect = mapCanvas.getBoundingClientRect();
        const x = (event.clientX - rect.left - this.panX) / zoom;
        const y = (event.clientY - rect.top - panY) / zoom;

        if(this.currentCanvasId === 'polyLineMapCanvas' ){
            const closestPolyLine = findClosestPolyLine(x,y);
            if(closestPolyLine != null){
                displayPolyLineProp(closestPolyLine.index);
            }
        }

        if(this.currentCanvasId === 'polygonMapCanvas'){
            const clickPoint = { x: x, y: y};
            for (let i = 0; i < feature.polygonGeom.length; i++) {
                if (isPointInPolygon(clickPoint, feature.polygonGeom[i])) {
                    displayPolygonProp(i);
                    break;
                }
            }
        }

        if(this.currentCanvasId === 'pointMapCanvas') {
            const clickRadius = 10; // 클릭 반경 설정
            const closePoints = feature.pointGeom
                .map((pointData, index) => {
                    const distance = Math.hypot(pointData.x - x, pointData.y - y);
                    return { pointData, distance, index };
                })
                .filter(point => point.distance <= clickRadius)
                .sort((a, b) => a.distance - b.distance)
                .map(point => feature.pointProp[point.index]);
            displayPointProp(closePoints);

        }
    }

    scalePoint(x, y, bbox, canvas) {
        let largestBbox = this.feature.largestBbox;

        if(bbox.minX===largestBbox.minX && bbox.minY===largestBbox.minY && bbox.maxX===largestBbox.maxX && bbox.maxY===largestBbox.maxY){
            let transformX = ((bbox.maxX - x) / (bbox.maxX - bbox.minX)) * canvas.width;
            let transformY = ((bbox.maxY - y) / (bbox.maxY - bbox.minY)) * canvas.height;
            transformX = canvas.width - transformX;
            return { x: transformX, y: transformY };
        }else{
            let adjustBbox = this.adjustBboxScale(bbox);
            let transformX = ((adjustBbox.maxX - x) / (adjustBbox.maxX - adjustBbox.minX)) * canvas.width;
            let transformY = ((adjustBbox.maxY - y) / (adjustBbox.maxY - adjustBbox.minY)) * canvas.height;
            transformX = canvas.width - transformX;
            return { x: transformX, y: transformY };
        }
    }

    adjustBboxScale(bbox) {
        let scaleX = (this.feature.largestBbox.maxX - feature.largestBbox.minX) / (bbox.maxX - bbox.minX);
        let scaleY = (this.feature.largestBbox.maxY - feature.largestBbox.minY) / (bbox.maxY - bbox.minY);
        return {
            minX: this.feature.largestBbox.minX + (bbox.minX - bbox.minX) * scaleX,
            minY: this.feature.largestBbox.minY + (bbox.minY - bbox.minY) * scaleY,
            maxX: this.feature.largestBbox.minX + (bbox.maxX - bbox.minX) * scaleX,
            maxY: this.feature.largestBbox.minY + (bbox.maxY - bbox.minY) * scaleY
        };
    }

    scrollHandler(event) {
        event.preventDefault();
        const scaleAmount = event.deltaY < 0 ? 1.1 : 0.9; // 줌 인과 줌 아웃에 대한 비율 조정
        const mapCanvas = document.getElementById(this.currentCanvasId);
        const cursorX = event.clientX - mapCanvas.getBoundingClientRect().left;
        const cursorY = event.clientY - mapCanvas.getBoundingClientRect().top;
        const worldX = (cursorX - this.panX) / this.zoom;
        const worldY = (cursorY - this.panY) / this.zoom;
        this.zoom *= scaleAmount;
        this.panX = cursorX - worldX * this.zoom;
        this.panY = cursorY - worldY * this.zoom;
        drawCanvas();
    }


    startDrag(event) {
        this.dragStart = { x: event.clientX - panX, y: event.clientY - panY };
    }

    onDrag(event) {
        if (this.dragStart !== null) {
            this.panX = event.clientX - this.dragStart.x;
            this.panY = event.clientY - this.dragStart.y;
            drawCanvas();
        }
    }

    endDrag() {
        this.dragStart = null;
    }
}
