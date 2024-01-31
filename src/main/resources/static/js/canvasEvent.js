let zoom = 1;
let panX = 0;
let panY = 0;
let dragStart = null;
let lineWidth = 1;
let pointSize = 1;

const rectWidth = 60;
const rectHeight = 60;

let currentRectBbox = { minX: 0, minY: 0, maxX: 0, maxY: 0 };
function scrollHandler(event) {
    let mapCanvas = document.getElementById('mapCanvas');
    event.preventDefault();
    const scaleAmount = event.deltaY < 0 ? 1.1 : 0.9;
    const cursorX = event.clientX - mapCanvas.getBoundingClientRect().left;
    const cursorY = event.clientY - mapCanvas.getBoundingClientRect().top;
    const worldX = (cursorX - panX) / zoom;
    const worldY = (cursorY - panY) / zoom;
    zoom *= scaleAmount;
    panX = cursorX - worldX * zoom;
    panY = cursorY - worldY * zoom;
    drawMap();
}

function startDrag(event) {
    dragStart = { x: event.clientX - panX, y: event.clientY - panY };
}

function onDrag(event) {
    if (dragStart !== null) {
        panX = event.clientX - dragStart.x;
        panY = event.clientY - dragStart.y;
        drawMap();
    }
}

function endDrag() {
    dragStart = null;
}

function drawRectangleWithCursor(event) {
    let mapCanvas = document.getElementById('mapCanvas');
    const rect = mapCanvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    const ctx = mapCanvas.getContext('2d');
    ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
    drawMap();

    currentRectBbox = {
        minX: x - rectWidth / 2,
        minY: y - rectHeight / 2,
        maxX: x + rectWidth / 2,
        maxY: y + rectHeight / 2
    };

    ctx.fillStyle = 'rgba(0, 0, 255, 0)';
    ctx.fillRect(currentRectBbox.minX, currentRectBbox.minY, rectWidth, rectHeight);

    ctx.strokeStyle = 'red';
    ctx.lineWidth = 3;
    ctx.strokeRect(currentRectBbox.minX, currentRectBbox.minY, rectWidth, rectHeight);

}



