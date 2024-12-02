
INSERT INTO direccion (altura, localidad, nombreCalle)
VALUES ('951', 'CABA', 'Medrano');
INSERT INTO heladera (capacidadEnViandas, viandasEnHeladera, fechaInicioFuncionamiento, nombre, temperaturaActualHeladera, direccion_idDireccion)
VALUES (123, 3,'2022-01-10', 'MedranoUTN', 12,
    (SELECT idDireccion
     FROM direccion
     WHERE nombreCalle = 'Medrano' AND localidad = 'CABA'));

INSERT INTO estadoheladera (estaActiva, fechaHoraInicio, heladera_idHeladera)
VALUES (1, '2024-06-10 12:00:00', (SELECT idHeladera FROM heladera
    WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10' ));

INSERT INTO ubicacion (latitud, longitud) VALUES(-34.5986207956, -58.4201153114);
INSERT INTO temperaturaheladera (temperaturaMaxima, temperaturaMinima) VALUES (100, 3);

UPDATE heladera
SET estadoHeladeraActual_idEstadoHeladera = (SELECT idEstadoHeladera
    FROM estadoheladera WHERE estaActiva = 1 and fechaHoraFin is null),
ubicacion_idUbicacion = (SELECT idUbicacion FROM ubicacion
    WHERE latitud = -34.5986207956 and longitud =-58.4201153114 ),
temperaturasDeFuncionamiento_idTemperaturaHeladera = (SELECT idTemperaturaHeladera
    FROM temperaturaheladera WHERE temperaturaMaxima = 100 AND temperaturaMinima = 3 )
where nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10';

INSERT INTO incidente (discriminator, fechaDeEmision, solucionado, tipoDeIncidente, tipoDeAlerta, heladera_idHeladera )
VALUES ('Alerta', '2024-12-02 17:34:54', 0, 'alerta temperatura minima', 'excede temperatura minima',
(SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10'));

INSERT INTO documento (numeroDocumento, tipoDocumento)
VALUES ('21244577', 'DNI'), ('43244597','DNI');

INSERT INTO tecnico (idrol,estaActivo, apellido, cuil) VALUES (1,1, 'Smith','20212445773');

INSERT INTO colaborador (idrol,estaActivo, puntuaje) VALUES (2,1, 128);

INSERT INTO persona (documento_idDocumento, rol_idrol)
VALUES ((SELECT idDocumento FROM documento WHERE numeroDocumento = '21244577'),
    (SELECT idrol FROM tecnico WHERE cuil = '20212445773' and apellido = 'Smith')),
    ((SELECT idDocumento FROM documento WHERE numeroDocumento = '43244597'),
         (SELECT idrol FROM colaborador WHERE puntuaje = 128 ));

INSERT INTO personahumana (apellido, nombre, id_persona)
VALUES ('Smith','Morty', (SELECT idPersona FROM persona p
    JOIN documento d ON d.idDocumento = p.documento_idDocumento
    WHERE d.numeroDocumento = '21244577')),
('Smith','Jerry', (SELECT idPersona FROM persona p
    JOIN documento d ON d.idDocumento = p.documento_idDocumento
    WHERE d.numeroDocumento = '43244597'));

INSERT INTO incidente (discriminator, fechaDeEmision, solucionado, tipoDeIncidente, descripcion, foto, colaborador_idPersona, heladera_idHeladera )
VALUES ('FallaTecnica', '2024-10-22 17:47:10', 0, 'Falla Técnica', 'Termostato de heladera no anda hay que cambiarlo','D:\hola\ \fotos\me.jpg',
(SELECT p.idPersona FROM persona p JOIN personahumana ph ON ph.id_persona=p.idPersona WHERE ph.nombre = 'Jerry' and ph.apellido='Smith'),
(SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10'));
