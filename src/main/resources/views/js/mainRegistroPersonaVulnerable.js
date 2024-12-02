document.addEventListener("DOMContentLoaded", function (){
    const tieneDomicilioSi = document.getElementById("domicilio-si");
    const tieneDomicilioNo = document.getElementById("domicilio-no");
    const domicilioValor = document.getElementById("domicilio-texto");

    const tieneMenoresSi = document.getElementById("menores-si");
    const tieneMenoresNo = document.getElementById("menores-no");
    const cantMenores = document.getElementById("cantidad-menores");

    const documento = document.getElementById('documento')
    const numeroDocumento = document.getElementById("numero-documento");


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

    configurarHabilitacion(tieneDomicilioSi, domicilioValor, "no");
    configurarHabilitacion(tieneMenoresSi, cantMenores, "no");
    configurarHabilitacion(documento, numeroDocumento, "noTiene");
});