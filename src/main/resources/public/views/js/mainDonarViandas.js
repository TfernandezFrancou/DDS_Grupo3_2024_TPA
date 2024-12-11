document.addEventListener("DOMContentLoaded", function (){
    const entregadoSi = document.getElementById("entregado-si");
    const entregadoNo = document.getElementById("entregado-no");
    const fechaEntregado = document.getElementById("fecha-entregado");


    // Función genérica para habilitar/deshabilitar inputs
    function configurarHabilitacion(triggerElement, targetElement, desactivarSiValor) {
        function toggleEstado() {
            if (triggerElement.type === "radio") {
                const radios = document.querySelectorAll(`input[name="${triggerElement.name}"]`);
                const valorSeleccionado = Array.from(radios).find(radio => radio.checked)?.value;
                targetElement.disabled = valorSeleccionado === desactivarSiValor;
            } else if (triggerElement.tagName === "SELECT") {
                targetElement.disabled = triggerElement.value === desactivarSiValor;
            }
        }

        // Asigna el evento según el tipo de elemento
        if (triggerElement.type === "radio") {
            const radios = document.querySelectorAll(`input[name="${triggerElement.name}"]`);
            radios.forEach(radio => radio.addEventListener("change", toggleEstado));
        } else if (triggerElement.tagName === "SELECT") {
            triggerElement.addEventListener("change", toggleEstado);
        }

        // Estado inicial
        toggleEstado();
    }

    configurarHabilitacion(entregadoSi, fechaEntregado, "no");

});