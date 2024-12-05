
function mostrarReporte(tipo) {

    document.querySelectorAll("button").forEach(btn => btn.classList.remove("active"));
    document.getElementById(`tab-${tipo}`).classList.add("active");

    const contenedor = document.getElementById("contenedor-reportes");
    contenedor.innerHTML = ""; // Limpiar contenido anterior

    // Llenar el contenedor según el tipo de reporte
    const items = reportes[tipo];
    if (tipo === "fallas") {
        items.forEach(item => {
            contenedor.innerHTML += `
                <a href="/fallas/${item.id}" class="report-item">
                    <span class="nombre-heladera">${item.nombre}</span>
                    <span class="fecha-registro">${item.fecha}</span>
                    <span class="direccion">${item.direccion}</span>
                    <span class="tipo-falla">${item.tipo}</span>
                </a>
            `;
        });
    } else if (tipo === "viandas-colocadas") {
        items.forEach(item => {
            contenedor.innerHTML += `
                <div class="report-item">
                    <span class="nombre-heladera">${item.nombre}</span>
                    <span class="direccion">Viandas Colocadas: ${item.colocadas}</span>
                </div>
            `;
        });
    } else if (tipo === "viandas-distribuidas") {
        items.forEach(item => {
            contenedor.innerHTML += `
                <div class="report-item">
                    <span class="nombre-heladera">Colaborador: ${item.colaborador}</span>
                    <span class="direccion">Viandas Distribuidas: ${item.viandas}</span>
                </div>
            `;
        });
    } else if (tipo === "viandas-retiradas") {
        items.forEach(item => {
            contenedor.innerHTML += `
                <div class="report-item">
                    <span class="nombre-heladera">${item.nombre}</span>
                    <span class="direccion">Viandas Retiradas: ${item.retiradas}</span>
                </div>
            `;
        });
    }
}

// Mostrar los reportes de fallas por defecto
mostrarReporte("fallas");
