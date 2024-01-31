let selectedPoints = [];
let selectedPolylines = [];
let selectedPolygons = [];

function onClick(event) {
    displayFeaturesInBBox(event);
}

function displayFeaturesInBBox(event) {
    let mapCanvas = document.getElementById('mapCanvas');
    let dataDisplay = document.getElementById('dataDisplay');
    dataDisplay.innerHTML = '';
    selectedPolygons = [];
    selectedPolylines = [];
    selectedPoints = [];
    const rect = mapCanvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    const canvasX = (x-panX) /zoom;
    const canvasY = (y-panY) /zoom;

    let featuresInBBox = [];

    // 포인트 확인
    points.forEach(point => {
        if (isPointInsideBBox(currentRectBbox, transformCoordinate(point.geometry.coordinatesInfo.coordinates[0]))) {
            featuresInBBox.push(point.properties.combinedInfo);
            selectedPoints.push(point);
        }
    });

    polyLines.forEach(polyline => {
        if (isPolylineIntersectingBBox(currentRectBbox, polyline.geometry.coordinatesInfo.coordinates)) {
            featuresInBBox.push(polyline.properties.combinedInfo);
            selectedPolylines.push(polyline);
        }
    });

    polygons.forEach(polygon => {
        if (isPolygonIntersectingBBox(currentRectBbox, polygon.geometry.coordinatesInfo.coordinates)) {
            featuresInBBox.push(polygon.properties.combinedInfo);
            selectedPolygons.push(polygon);
            return;
        }
        if (isPointInPolygon({x:canvasX, y:canvasY}, polygon.geometry.coordinatesInfo.coordinates)) {
            featuresInBBox.push(polygon.properties.combinedInfo);
            selectedPolygons.push(polygon);
        }
    });

    dataDisplay.innerHTML = JSON.stringify(featuresInBBox, null, 2);

    const ctx = mapCanvas.getContext('2d');
    drawPolygon(ctx, selectedPolygons, 'yellow');
    drawPolyLine(ctx, selectedPolylines, 'red');
    drawPoint(ctx, selectedPoints, 'maroon');
}

function doLineSegmentsIntersect(a, b, c, d) {
    function crossProduct(p1, p2, p3) {
        return (p3.y - p1.y) * (p2.x - p1.x) - (p2.y - p1.y) * (p3.x - p1.x);
    }

    function isPointOnLine(a, b, c) {
        return Math.min(a.x, b.x) <= c.x && c.x <= Math.max(a.x, b.x) &&
            Math.min(a.y, b.y) <= c.y && c.y <= Math.max(a.y, b.y);
    }

    const d1 = crossProduct(a, b, c);
    const d2 = crossProduct(a, b, d);
    const d3 = crossProduct(c, d, a);
    const d4 = crossProduct(c, d, b);

    if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
        ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
        return true;
    }

    if (d1 === 0 && isPointOnLine(a, b, c)) return true;
    if (d2 === 0 && isPointOnLine(a, b, d)) return true;
    if (d3 === 0 && isPointOnLine(c, d, a)) return true;
    if (d4 === 0 && isPointOnLine(c, d, b)) return true;

    return false;
}

function isPointInPolygon(point, polygon) {
    let inside = false;
    for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
        const xi = polygon[i].x, yi = polygon[i].y;
        const xj = polygon[j].x, yj = polygon[j].y;

        const intersect = ((yi > point.y) !== (yj > point.y)) &&
            (point.x < (xj - xi) * (point.y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }
    return inside;
}

function isPolylineIntersectingBBox(bbox, polyline) {
    for (let i = 0; i < polyline.length - 1; i++) {
        const segmentStart = transformCoordinate(polyline[i]);
        const segmentEnd = transformCoordinate(polyline[i + 1]);

        if (doLineSegmentsIntersect(segmentStart, segmentEnd, {x: bbox.minX, y: bbox.minY}, {x: bbox.maxX, y: bbox.minY}) ||
            doLineSegmentsIntersect(segmentStart, segmentEnd, {x: bbox.maxX, y: bbox.minY}, {x: bbox.maxX, y: bbox.maxY}) ||
            doLineSegmentsIntersect(segmentStart, segmentEnd, {x: bbox.maxX, y: bbox.maxY}, {x: bbox.minX, y: bbox.maxY}) ||
            doLineSegmentsIntersect(segmentStart, segmentEnd, {x: bbox.minX, y: bbox.maxY}, {x: bbox.minX, y: bbox.minY})) {
            return true;
        }

        if(isPointInsideBBox(bbox, segmentStart) && isPointInsideBBox(bbox, segmentEnd)){
            return true;
        }
    }
    return false;
}

function isPolygonIntersectingBBox(bbox, polygon) {
    for (const vertex of polygon) {
        const transformedVertex = transformCoordinate(vertex);

        if (isPointInsideBBox(bbox, transformedVertex)) {
            return true;
        }
    }
    if(isPolylineIntersectingBBox(bbox, polygon)){
        return true;
    }
}

function isPointInsideBBox(bbox, point) {
    return point.x >= bbox.minX && point.x <= bbox.maxX && point.y >= bbox.minY && point.y <= bbox.maxY;
}

function transformCoordinate(coordinate) {
    return {
        x: (coordinate.x * zoom) + panX,
        y: (coordinate.y * zoom) + panY
    };
}