import {CanvasBase} from './CanvasBase';
export class CanvasPoint extends CanvasBase{
    async pointInit() {
        if (!super.feature.pointObj) {
            const response = await fetch('/mapData/shp/Duraklar');
            if (response.ok) {
                const responseData = await response.json();
                feature.pointObj = responseData.geometry;
                feature.pointProp = responseData.property.records;
                super.updateLargestBBox(responseData.geometry.bbox);
            }
        }
    }

    async drawPointCanvasMap(){
        const shapeData = await feature.pointObj;
        const points = await shapeData.points;
        const bbox = await  shapeData.bbox;

        const mapCanvas = document.getElementById('pointMapCanvas');
        if(mapCanvas.getContext) {
            const ctx = mapCanvas.getContext('2d');
            ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
            ctx.save();
            ctx.translate(panX, panY);
            ctx.scale(zoom, zoom);
            ctx.fillStyle='blue';

            points.forEach((point, index) => {
                let trPoint = super.scalePoint(point.x, point.y, bbox, mapCanvas);
                ctx.beginPath();
                ctx.arc(trPoint.x, trPoint.y, 0.5, 0, 2 * Math.PI);
                ctx.fill();
                feature.pointGeom.push({x:trPoint.x, y:trPoint.y, index: index});
            });
            ctx.restore();
        }
    }

    displayPointAttr(closePoints){
        document.getElementById('dataDisplay').innerHTML = closePoints.map(attr => JSON.stringify(attr, null, 2)).join('<br><br>');
    }
}
