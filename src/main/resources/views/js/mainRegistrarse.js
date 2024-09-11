document.addEventListener("DOMContentLoaded", function () {
    const personaToggle = document.getElementById("personaToggle");
    const razonSocialFields = document.getElementById("razonSocialFields");
    const nombreField = document.getElementById("nombre").parentElement;
    const apellidoField = document.getElementById("apellido").parentElement;
    const fechaNacimientoField = document.getElementById("fechaNacimiento").parentElement;

    // Colaboraciones
    const donarVianda = document.getElementById("donarVianda");
    const distribuirViandas = document.getElementById("distribuirViandas");
    const registrarPersonaEnSituaciónVulnerable = document.getElementById("registrarPersonasSituacionVulnerable");
    const ofrecerProductos = document.getElementById("ofrecerProductos");

    personaToggle.addEventListener("change", function () {
        if (personaToggle.checked) {
            // Cambios para Persona Jurídica
            nombreField.style.display = "none";
            apellidoField.style.display = "none";
            fechaNacimientoField.style.display = "none";

            razonSocialFields.style.display = "block"; // Mostrar campos de razón social

            // Ocultar colaboraciones específicas
            donarVianda.style.display = "none";
            distribuirViandas.style.display = "none";
            registrarPersonaEnSituaciónVulnerable.style.display = "none";

            // Mostrar "Ofrecer Productos"
            ofrecerProductos.style.display = "block";
        } else {
            // Revertir a Persona Humana
            nombreField.style.display = "block";
            apellidoField.style.display = "block";
            fechaNacimientoField.style.display = "block";

            razonSocialFields.style.display = "none"; // Ocultar campos de razón social

            // Mostrar colaboraciones originales
            donarVianda.style.display = "block";
            distribuirViandas.style.display = "block";
            registrarPersonaEnSituaciónVulnerable.style.display = "block";

            // Ocultar "Ofrecer Productos"
            ofrecerProductos.style.display = "none";
        }
    });

    //placeholder de medios de contacto

    const tipoContacto = document.getElementById("tipoContacto");
    const contactoInput = document.getElementById("contacto");

    tipoContacto.addEventListener("change", function () {
        const selectedValue = tipoContacto.value;

        switch (selectedValue) {
            case "email":
                contactoInput.placeholder = "ejemplo@correo.com";
                break;
            case "telefono":
                contactoInput.placeholder = "123-456-7890";
                break;
            case "whatsapp":
                contactoInput.placeholder = "+12 345 678 910";
                break;
            default:
                contactoInput.placeholder = "Introduce tu contacto";
        }
    });

    // agregar medios de contacto 

    const agregarContactoBtn = document.getElementById("agregarContacto");
    const mediosContactoContainer = document.getElementById("mediosContactoContainer");
    const quitarContactoBtn = document.getElementById("quitarContacto");

    // Función para verificar el número de medios de contacto
    function verificarBotonQuitar() {
        const totalGrupos = mediosContactoContainer.querySelectorAll(".contacto-grupo").length;
        quitarContactoBtn.classList.toggle("disabled-link", totalGrupos <= 2);  // Desactiva el botón si hay <= 1 medio
    }

    let counter = 0;

    agregarContactoBtn.addEventListener("click", function (e) {
        e.preventDefault();

        counter++;

        const nuevoContactDivDiv = document.createElement("div");
        nuevoContactDivDiv.classList.add("col-4", "contacto-grupo");

        // Crear nuevo div contenedor para el nuevo medio de contacto
        const nuevoContactoDiv = document.createElement("div");
        nuevoContactoDiv.classList.add("form-group", "mb-3");

        // Crear el select para el nuevo tipo de contacto
        const nuevoSelect = document.createElement("select");
        nuevoSelect.classList.add("form-select");
        nuevoSelect.setAttribute("id", "tipoContacto" + counter);
        nuevoSelect.innerHTML = `
            <option value="" disabled selected>Seleccionar</option>
            <option value="email">Email</option>
            <option value="telefono">Teléfono</option>
            <option value="whatsapp">WhatsApp</option>
        `;

        // Crear el input para el nuevo contacto
        const nuevoInput = document.createElement("input");
        nuevoInput.type = "text";
        nuevoInput.classList.add("form-control", "rounded-pill");
        nuevoInput.placeholder = "Introduce tu contacto";

        // Agregar el evento 'change' para actualizar el placeholder
        nuevoSelect.addEventListener("change", function () {
            const selectedValue = nuevoSelect.value;

            switch (selectedValue) {
                case "email":
                    nuevoInput.placeholder = "ejemplo@correo.com";
                    break;
                case "telefono":
                    nuevoInput.placeholder = "123-456-7890";
                    break;
                case "whatsapp":
                    nuevoInput.placeholder = "+12 345 678 910";
                    break;
                default:
                    nuevoInput.placeholder = "Introduce tu contacto";
            }
        });



        // Agregar los elementos creados al contenedor
        nuevoContactoDiv.appendChild(nuevoSelect);
        nuevoContactDivDiv.appendChild(nuevoContactoDiv);
        mediosContactoContainer.appendChild(nuevoContactDivDiv);


        const nuevoInputDivDiv = document.createElement("div");
        nuevoInputDivDiv.classList.add("col-8", "contacto-grupo");
        // Crear un nuevo div para el input de contacto
        const nuevoInputDiv = document.createElement("div");
        nuevoInputDiv.classList.add("form-group", "mb-3");
        nuevoInputDiv.appendChild(nuevoInput);

        nuevoInputDivDiv.appendChild(nuevoInputDiv);
        mediosContactoContainer.appendChild(nuevoInputDivDiv);

        verificarBotonQuitar(); // Verificar si se debe habilitar el botón de quitar
    });

    // quitar medios de contacto

    // Evento para quitar un medio de contacto
    quitarContactoBtn.addEventListener("click", function (e) {
        e.preventDefault();
        counter--;

        const totalGrupos = mediosContactoContainer.querySelectorAll(".contacto-grupo");

        if (totalGrupos.length > 2) {
            // Remover los últimos dos elementos (select + input)
            mediosContactoContainer.removeChild(totalGrupos[totalGrupos.length - 1]);
            mediosContactoContainer.removeChild(totalGrupos[totalGrupos.length - 2]);
        }

        verificarBotonQuitar(); // Verificar si se debe deshabilitar el botón de quitar
    });

    verificarBotonQuitar(); // Inicializar la verificación del botón al cargar

});
