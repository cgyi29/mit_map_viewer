let featureCollection = null;

let polyLines = [];
let polygons = [];
let points = [];

async function featureCollectionInit() {
    let mapCanvas = document.getElementById('mapCanvas');
    if (!featureCollection) {
        const res = await fetch('/v2/map/feature/kr/'+mapCanvas.width+'/'+mapCanvas.height);
        if (res.ok) {
            const resData = await res.json();
            featureCollection = Object.entries(resData)[0][1];
            featureCollection.forEach(feature => {
                if(feature.geometry != null){
                    switch (feature.geometry.type) {
                        case 'POINT': points.push(feature); break;
                        case 'POLYLINE': polyLines.push(feature); break;
                    }
                }else{
                    polygons.push(feature);
                }
            });
            /*console.log(polyLines);
            console.log(polygons);
            console.log(points);*/
        }
    }
}

function drawPoint(ctx, pointsArr, color){
    if(pointsArr.length === 0) return;
    let mapCanvas = document.getElementById('mapCanvas');
    if(mapCanvas.getContext) {
        pointSize = 1.5/zoom;
        ctx.save();
        ctx.translate(panX, panY);
        ctx.scale(zoom, zoom);
        ctx.fillStyle=color;
        pointsArr.forEach((feature) => {
            feature.geometry.coordinatesInfo.coordinates.forEach((coordinate) => {
                ctx.beginPath();
                ctx.arc(coordinate.x, coordinate.y, pointSize, 0, 2 * Math.PI);
                ctx.fill();
            });
        });
        ctx.restore();
    }
}

function drawPolyLine(ctx, polylineArr, color) {
    if(polylineArr.length === 0) return;
    let mapCanvas = document.getElementById('mapCanvas');
    if (mapCanvas.getContext) {
        lineWidth = 1/zoom;
        ctx.save();
        ctx.translate(panX, panY);
        ctx.scale(zoom, zoom);
        ctx.lineWidth = lineWidth;
        ctx.strokeStyle = color;

        polylineArr.forEach((feature) => {
            const coordinates = feature.geometry.coordinatesInfo.coordinates;
            const parts = feature.geometry.coordinatesInfo.parts;
            parts.forEach((start, partIndex) => {
                ctx.beginPath();
                let end = coordinates.length;
                if (partIndex < parts.length - 1) {
                    end = parts[partIndex + 1];
                }
                for (let i = start; i < end; i++) {
                    i === start ? ctx.moveTo(coordinates[i].x, coordinates[i].y): ctx.lineTo(coordinates[i].x, coordinates[i].y);
                }
                ctx.stroke();
            });
        });
        ctx.restore();
    }
}

function drawPolygon(ctx, polygonArr, color){
    if(polygonArr.length === 0) return;
    let mapCanvas = document.getElementById('mapCanvas');
    if (mapCanvas.getContext) {
        lineWidth = 1/zoom;
        ctx.save();
        ctx.translate(panX, panY);
        ctx.scale(zoom, zoom);
        ctx.lineWidth = lineWidth;
        ctx.fillStyle = color;
        ctx.strokeStyle = 'black'; // 선 색상 추가

        polygonArr.forEach((feature) => {
            const geometryList = feature.geometryList;
            ctx.beginPath(); // 각 다각형 시작 시에 한 번만 호출
            geometryList.forEach((geometry) => {
                const coordinates = geometry.coordinatesInfo.coordinates;
                coordinates.forEach((coordinate, index) => {
                    index === 0 ? ctx.moveTo(coordinate.x, coordinate.y) : ctx.lineTo(coordinate.x, coordinate.y);
                })

            });
            ctx.closePath(); // 다각형 닫기
            ctx.fill();
            ctx.stroke();
        });
        ctx.restore();
    }
}

function drawMap(){
    let mapCanvas = document.getElementById('mapCanvas');
    const ctx = mapCanvas.getContext('2d');
    ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
    drawPolygon(ctx, polygons, 'pink');
    drawPolyLine(ctx, polyLines, 'green');
    drawPoint(ctx, points, 'blue' );
}