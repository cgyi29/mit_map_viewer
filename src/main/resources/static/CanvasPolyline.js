import {CanvasBase} from "./CanvasBase";
export class CanvasPolyline extends CanvasBase{
    async polyLineInit(){
        if (!feature.polyLineObj) {
            const response = await fetch('/mapData/shp/Hatlar');
            if (response.ok) {
                const responseData = await response.json();
                feature.polyLineObj = responseData.geometry;
                feature.polyLineProp = responseData.property.records;
                super.updateLargestBBox(responseData.geometry.bbox);
            }
        }
    }

    async drawPolyLineCanvasMap(){
        const shapeData = await feature.polyLineObj;
        const polyLineData = await shapeData.polyTypeDatas;
        const bbox = await  shapeData.bbox;

        const mapCanvas = await document.getElementById('polyLineMapCanvas');
        if(mapCanvas.getContext){
            const ctx = await mapCanvas.getContext('2d');
            ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
            ctx.save();
            ctx.translate(panX, panY);
            ctx.scale(zoom, zoom);
            ctx.strokeStyle='red';

            polyLineData.forEach(function(data){
                data.parts.forEach(function() {
                    ctx.beginPath();
                    let transformedPoint = [];
                    data.points.forEach((pointData, index) => {
                        let point = super.scalePoint(pointData.x, pointData.y, bbox, mapCanvas);
                        transformedPoint.push(point);
                        index === 0 ? ctx.moveTo(point.x, point.y) : ctx.lineTo(point.x, point.y);
                    });
                    ctx.stroke();
                    feature.polyLineGeom.push(transformedPoint);
                });
            });
            ctx.restore();
        }
    }

    displayPolyLineAttr(idx){
        const attrData = feature.polyLineProp[idx];
        if (attrData) {
            document.getElementById('dataDisplay').innerText = JSON.stringify(attrData, null, 2);
        }

    }

    findClosestPolyLine(clickX, clickY) {
        let minDistance = Infinity;
        let closestPolyLineIndex = null;

        feature.polyLineGeom.forEach((line, index) => {
            for (let i = 0; i < line.length - 1; i++) {
                let distance = this.distanceToLineSegment(clickX, clickY, line[i].x, line[i].y, line[i + 1].x, line[i + 1].y);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPolyLineIndex = index;
                }
            }
        });

        return closestPolyLineIndex != null ? { index: closestPolyLineIndex } : null;
    }

    distanceToLineSegment(x, y, x1, y1, x2, y2) {
        const A = x - x1;
        const B = y - y1;
        const C = x2 - x1;
        const D = y2 - y1;


        const dot = A * C + B * D;
        const len_sq = C * C + D * D;
        let param = -1;
        if (len_sq !== 0) {param = dot / len_sq; }

        let xx, yy;
        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        const dx = x - xx;
        const dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
