document.addEventListener("DOMContentLoaded", function () {
    const personaToggle = document.getElementById("personaToggle");
    const razonSocial = document.getElementById("razonSocial");
    const rubro = document.getElementById("rubro");
    const tipoRazonSocialField = document.getElementById("tipoRazonSocial");
    const razonSocialFields = document.getElementById("razonSocialFields");

    const nombreFieldParent = document.getElementById("nombre").parentElement;
    const apellidoFieldParent = document.getElementById("apellido").parentElement;
    const fechaNacimientoFieldParent = document.getElementById("fechaNacimiento").parentElement;

    const nombreField = document.getElementById("nombre");
    const apellidoField = document.getElementById("apellido");
    const fechaNacimientoField = document.getElementById("fechaNacimiento");

    // Colaboraciones
    const donarVianda = document.getElementById("donarVianda");
    const donarViandaParent = document.getElementById("donarViandaParent");
    const distribuirViandas = document.getElementById("distribuirViandas");
    const distribuirViandasParent = document.getElementById("distribuirViandasParent");
    const registrarPersonasSituacionVulnerable = document.getElementById("registrarPersonasSituacionVulnerable");
    const registrarPersonasSituacionVulnerableParent = document.getElementById("registrarPersonasSituacionVulnerableParent");
    const ofrecerProductos = document.getElementById("ofrecerProductos");
    const ofrecerProductosParent = document.getElementById("ofrecerProductosParent");
    function resetElements(){
        if (personaToggle.checked) {
                // Cambios para Persona Jurídica

                nombreField.removeAttribute("name");
                nombreFieldParent.style.display = "none";
                nombreField.setAttribute("disabled", true);

                apellidoField.removeAttribute("name");
                apellidoFieldParent.style.display = "none";
                apellidoField.setAttribute("disabled", true);


                fechaNacimientoField.removeAttribute("name");
                fechaNacimientoFieldParent.style.display = "none";
                fechaNacimientoField.setAttribute("disabled", true);


                razonSocialFields.style.display = "block"; // Mostrar campos de razón social
                razonSocialFields.setAttribute("name", "razonSocialFields");
                razonSocialFields.removeAttribute("disabled");

                razonSocial.style.display = "block"; // Mostrar campos de razón social
                razonSocial.setAttribute("name", "razonSocial");
                razonSocial.removeAttribute("disabled");

                tipoRazonSocialField.style.display = "block"
                tipoRazonSocialField.setAttribute("name", "tipoRazonSocial")
                tipoRazonSocialField.removeAttribute("disabled")

                rubro.style.display = "block"
                rubro.setAttribute("name", "rubro")
                rubro.removeAttribute("disabled")

                // Ocultar colaboraciones específicas
                donarVianda.removeAttribute("name");
                donarVianda.style.display = "none";
                donarVianda.setAttribute("disabled", true);

                donarViandaParent.style.display = "none";
                donarViandaParent.setAttribute("disabled", true);

                distribuirViandas.removeAttribute("name");
                distribuirViandas.style.display = "none";
                distribuirViandas.setAttribute("disabled", true);

                distribuirViandasParent.style.display = "none";
                distribuirViandasParent.setAttribute("disabled", true);

                registrarPersonasSituacionVulnerable.removeAttribute("name");
                registrarPersonasSituacionVulnerable.style.display = "none";
                registrarPersonasSituacionVulnerable.setAttribute("disabled", true);

                registrarPersonasSituacionVulnerableParent.style.display = "none";
                registrarPersonasSituacionVulnerableParent.setAttribute("disabled", true);

                // Mostrar "Ofrecer Productos"
                ofrecerProductos.style.display = "block";
                ofrecerProductos.setAttribute("name", "ofrecerProductos");
                ofrecerProductos.removeAttribute("disabled");

                ofrecerProductosParent.style.display = "block";
                ofrecerProductosParent.removeAttribute("disabled");

            } else {
                // Revertir a Persona Humana
                nombreFieldParent.style.display = "block";
                nombreField.setAttribute("name", "nombre");
                nombreField.removeAttribute("disabled");

                apellidoFieldParent.style.display = "block";
                apellidoField.setAttribute("name", "apellido");
                apellidoField.removeAttribute("disabled");

                fechaNacimientoFieldParent.style.display = "block";
                fechaNacimientoField.setAttribute("name", "fechaNacimiento");
                fechaNacimientoField.removeAttribute("disabled");

                razonSocialFields.removeAttribute("name");
                razonSocialFields.style.display = "none"; // Ocultar campos de razón social
                razonSocialFields.setAttribute("disabled", true);

                razonSocial.removeAttribute("name");
                razonSocial.style.display = "none"; // Ocultar campos de razón social
                razonSocial.setAttribute("disabled", true);

                tipoRazonSocialField.removeAttribute("name")
                tipoRazonSocialField.style.display = "none"
                tipoRazonSocialField.setAttribute("disabled", true)

                rubro.removeAttribute("name")
                rubro.style.display = "none"
                rubro.setAttribute("disabled", true)

                // Mostrar colaboraciones originales
                donarVianda.style.display = "block";
                donarVianda.setAttribute("name", "donarVianda");
                donarVianda.removeAttribute("disabled");

                donarViandaParent.style.display="block";
                donarViandaParent.removeAttribute("disabled");

                distribuirViandas.style.display = "block";
                distribuirViandas.setAttribute("name", "distribuirViandas");
                distribuirViandas.removeAttribute("disabled");

                distribuirViandasParent.removeAttribute("disabled");
                distribuirViandasParent.style.display="block";

                registrarPersonasSituacionVulnerable.style.display = "block";
                registrarPersonasSituacionVulnerable.setAttribute("name", "registrarPersonasSituacionVulnerable");
                registrarPersonasSituacionVulnerable.removeAttribute("disabled");

                registrarPersonasSituacionVulnerableParent.removeAttribute("disabled");
                registrarPersonasSituacionVulnerableParent.style.display="block";

                // Ocultar "Ofrecer Productos"
                ofrecerProductos.removeAttribute("name");
                ofrecerProductos.style.display = "none";
                ofrecerProductos.setAttribute("disabled", true);

                ofrecerProductosParent.style.display = "none";
                ofrecerProductosParent.setAttribute("disabled", true);
            }
    }

    resetElements()//reset inicial

    personaToggle.addEventListener("change", resetElements);

    //placeholder de medios de contacto

    const tipoContacto = document.getElementById("tipoContacto");
    const contactoInput = document.getElementById("contacto");

    function actualizarPlaceHolderContactoSegun(tipoContacto, contactoInput) {
        return function(){
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
        }
    }

    tipoContacto.addEventListener("change", actualizarPlaceHolderContactoSegun(tipoContacto, contactoInput));

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
        nuevoSelect.setAttribute("name", "tipoContacto" /*+ counter*/);
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
        nuevoInput.setAttribute("name","contacto"/*+ counter*/);

        // Agregar el evento 'change' para actualizar el placeholder
        nuevoSelect.addEventListener("change", actualizarPlaceHolderContactoSegun(nuevoSelect,nuevoInput ));

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

document.getElementById('form').addEventListener('submit', function (e) {
    this.querySelectorAll('input').forEach(input => {
        if (input.disabled) {
            input.removeAttribute('name');  // Elimina el 'name' para evitar que se envíe
        }
    });

    document.getElementById("nombre").setAttribute("name", "nombre");
});