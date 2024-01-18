import {CanvasBase} from "./CanvasBase";

export class CanvasPolygon extends CanvasBase{
    async polygonInit(){
        if (!feature.polygonObj) {
            const response = await fetch('/mapData/shp/KoyMahalle');
            if (response.ok) {
                const responseData = await response.json();
                feature.polygonObj = responseData.geometry;
                feature.polygonProp = responseData.property.records;
                super.updateLargestBBox(responseData.geometry.bbox);
            }
        }
    }

    async drawPolygonCanvasMap(){
        const shapeData =  feature.polygonObj;
        const polygonData = await shapeData.polyTypeDatas;
        const bbox = await  shapeData.bbox;

        const mapCanvas = await document.getElementById('polygonMapCanvas');
        if(mapCanvas.getContext){
            const ctx = await mapCanvas.getContext('2d');
            ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
            ctx.save();
            ctx.translate(panX, panY);
            ctx.scale(zoom, zoom);

            polygonData.forEach(function(data){
                data.parts.forEach(function() {
                    ctx.beginPath();

                    let transformedPoint = [];
                    data.points.forEach((pointData, index) => {
                        let point = super.scalePoint(pointData.x, pointData.y, bbox, mapCanvas);
                        transformedPoint.push(point);
                        if (index === 0) {
                            ctx.moveTo(point.x, point.y);
                        } else {
                            ctx.lineTo(point.x, point.y);
                        }
                    });
                    ctx.fillStyle = "yellow";
                    ctx.fill();
                    ctx.stroke();
                    feature.polygonGeom.push(transformedPoint);
                });
            });
            ctx.restore();
        }
    }


    displayPolygonAttr(idx){
        const attrData = feature.polygonProp[idx];
        if (attrData) {
            document.getElementById('dataDisplay').innerText = JSON.stringify(attrData, null, 2);
        }

    }

    isPointInPolygon(point, polygon) {
        let x = point.x, y = point.y;
        let inside = false;
        for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            let xi = polygon[i].x, yi = polygon[i].y;
            let xj = polygon[j].x, yj = polygon[j].y;
            let intersect = ((yi > y) !== (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
            if (intersect) inside = !inside;
        }
        return inside;
    }
}
