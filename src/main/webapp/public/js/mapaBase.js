function crearMapaBase(elementId = 'map') {
    const map = L.map(elementId, {
        maxBounds: [[-90, -180], [90, 180]],
        maxBoundsViscosity: 1,
        maxZoom: 8,
        minZoom: 3
    }).setView([20, 0], 3);

    L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Physical_Map/MapServer/tile/{z}/{y}/{x}', {
        attribution: 'Tiles &copy; Esri',
        noWrap: true,
        bounds: [[-90, -180], [90, 180]]
    }).addTo(map);

    return map;
}
