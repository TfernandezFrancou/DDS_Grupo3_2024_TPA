
function mostrarReporte(tipo) {

    document.querySelectorAll("button").forEach(btn => btn.classList.remove("active"));
    document.getElementById(`tab-${tipo}`).classList.add("active");

    fetch(`/reportes/${tipo}`)
        .then(response => response.json())
        .then(data => {
            const contenedor = document.getElementById("contenedor-reportes");
            contenedor.innerHTML = ""; // Limpia el contenido actual

            // Renderiza los datos dinámicamente según el tipo de reporte
            data.reportes.forEach(item => {
                if (tipo === "fallas") {
                    // Renderizar reportes de fallas
                    contenedor.innerHTML += `
                        <div class="card mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Heladera: ${item.heladera.nombre}</h5>
                                <p class="card-text">Fallas: ${item.fallas.map(f => f.descripcion).join(", ")}</p>
                            </div>
                        </div>
                    `;
                } else if (tipo === "viandas-colocadas") {
                    // Renderizar reportes de viandas colocadas
                    contenedor.innerHTML += `
                        <div class="card mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Heladera: ${item.heladera.nombre}</h5>
                                <p class="card-text">Viandas Colocadas: ${item.viandasColocadas.length}</p>
                            </div>
                        </div>
                    `;
                } else if (tipo === "viandas-retiradas") {
                    // Renderizar reportes de viandas retiradas
                    contenedor.innerHTML += `
                        <div class="card mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Heladera: ${item.heladera.nombre}</h5>
                                <p class="card-text">Viandas Retiradas: ${item.viandasRetiradas.length}</p>
                            </div>
                        </div>
                    `;
                } else if (tipo === "viandas-distribuidas") {
                    // Renderizar reportes de viandas distribuidas por colaborador
                    contenedor.innerHTML += `
                        <div class="card mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Colaborador: ${item.colaborador.nombre}</h5>
                                <p class="card-text">Viandas Distribuidas: ${item.viandasDistribuidas.length}</p>
                            </div>
                        </div>
                    `;
                }
            });
        })
        .catch(error => {
            console.error("Error al cargar los reportes:", error);
            const contenedor = document.getElementById("contenedor-reportes");
            contenedor.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    No se pudieron cargar los reportes. Intenta nuevamente.
                </div>
            `;
        });
}
mostrarReporte('fallas');
