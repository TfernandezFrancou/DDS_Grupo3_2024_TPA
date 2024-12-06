
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
VALUES ('21244577', 'DNI'), ('43244597','DNI'), ('43244566', 'DNI');

-- Obtener el próximo valor para tecino
SET @next_id_tecnico = (SELECT next_val FROM rol_sequence);
UPDATE rol_sequence SET next_val = next_val + 1;

INSERT INTO tecnico (idrol,estaActivo, apellido, cuil) VALUES (@next_id_tecnico,1, 'Smith','20212445773');

-- Obtener el próximo valor para colaborador
SET @next_id_colaborador = (SELECT next_val FROM rol_sequence);
UPDATE rol_sequence SET next_val = next_val + 1;

INSERT INTO colaborador (idrol,estaActivo, puntuaje) VALUES (@next_id_colaborador,1, 128);

INSERT INTO persona (documento_idDocumento, rol_idrol)
VALUES ((SELECT idDocumento FROM documento WHERE numeroDocumento = '21244577'),
    (SELECT idrol FROM tecnico WHERE cuil = '20212445773' and apellido = 'Smith')),
    ((SELECT idDocumento FROM documento WHERE numeroDocumento = '43244597'),
         (SELECT idrol FROM colaborador WHERE puntuaje = 128 and estaActivo = 1));

INSERT INTO personahumana (apellido, nombre, id_persona)
VALUES ('Smith','Morty', (SELECT idPersona FROM persona p
    JOIN documento d ON d.idDocumento = p.documento_idDocumento
    WHERE d.numeroDocumento = '21244577')),
('Smith','Jerry', (SELECT idPersona FROM persona p
    JOIN documento d ON d.idDocumento = p.documento_idDocumento
    WHERE d.numeroDocumento = '43244597'));

INSERT INTO incidente (discriminator, fechaDeEmision, solucionado, tipoDeIncidente, descripcion, foto, colaborador_idPersona, heladera_idHeladera )
VALUES ('FallaTecnica', '2024-10-22 17:47:10', 0, 'Falla Técnica', 'Termostato de heladera no anda hay que cambiarlo','https://img.freepik.com/vector-premium/refrigerador-congelador-domestico-almacenamiento-alimentos-icono-vector-estilo-arte-linea-aislar-blanco_456865-713.jpg',
(SELECT p.idPersona FROM persona p JOIN personahumana ph ON ph.id_persona=p.idPersona WHERE ph.nombre = 'Jerry' and ph.apellido='Smith'),
(SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10'));

INSERT INTO entrega (estadoEntrega, fechaEntrega)
VALUES ('ENTREGADO', '2024-12-03'),('ENTREGADO', '2024-12-04');

INSERT INTO vianda (calorias, descripcion, fechaCaducidad, fechaDonacion,peso, colaborador_idrol,entrega_idEntrega, heladera_idHeladera)
VALUES (200, 'ensalada de tomate', '2102-10-22 00:00:00','2024-12-02 16:00:00', 20,
 (SELECT idrol FROM colaborador WHERE puntuaje = 128 and estaActivo = 1),
 (SELECT idEntrega FROM entrega where estadoEntrega = 'ENTREGADO' and fechaEntrega = '2024-12-03'),
 (SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10')),
 (1000, 'pancho con coca', '2040-10-22 00:00:00','2024-12-03 16:00:00', 10,
  (SELECT idrol FROM colaborador WHERE puntuaje = 128 and estaActivo = 1),(SELECT idEntrega FROM entrega where estadoEntrega = 'ENTREGADO' and fechaEntrega = '2024-12-04'),
  (SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10'));
;


INSERT INTO reportesdelasemana (fechaFinSemana, fechaInicioSemana) VALUES ('2024-12-31', '2024-12-01');

 INSERT INTO itemreporte (id_reporte_de_la_semana)
 VALUES ((SELECT idReportesDeLaSemana FROM reportesdelasemana WHERE fechaInicioSemana = '2024-12-01'));

 INSERT INTO itemreportefallasporheladera (id_item_reporte, heladera_idHeladera)
 VALUES ((SELECT idItemReporte FROM itemreporte ir
    JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
    WHERE rs.fechaInicioSemana = '2024-12-01'),
 (SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10')
 );

 INSERT INTO itemreporteviandascolocadasporheladera (id_item_reporte, heladera_idHeladera)
 VALUES ((SELECT idItemReporte FROM itemreporte ir
    JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
    WHERE rs.fechaInicioSemana = '2024-12-01'),
 (SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10')
 );

INSERT INTO itemreporteviandascolocadasporheladeraxvianda(ItemReporteViandasColocadasPorHeladera_id_item_reporte, viandasColocadas_idVianda)
VALUES ((SELECT idItemReporte FROM itemreporte ir
            JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
            WHERE rs.fechaInicioSemana = '2024-12-01'),
(SELECT idVianda FROM vianda v WHERE v.descripcion = 'ensalada de tomate'));

INSERT INTO itemreporteviandasdistribuidasporcolaborador (id_item_reporte, colaborador_idPersona)
VALUES ((SELECT idItemReporte FROM itemreporte ir
    JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
    WHERE rs.fechaInicioSemana = '2024-12-01'),
(SELECT p.idPersona FROM persona p JOIN personahumana ph ON ph.id_persona=p.idPersona WHERE ph.nombre = 'Jerry' and ph.apellido='Smith')
);

INSERT INTO itemreporteviandasdistribuidasporcolaboradorxvianda (ItemReporteViandasDistribuidasPorColaborador_id_item_reporte, viandasDistribuidas_idVianda)
VALUES ((SELECT idItemReporte FROM itemreporte ir
                    JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
                    WHERE rs.fechaInicioSemana = '2024-12-01'),
        (SELECT idVianda FROM vianda v WHERE v.descripcion = 'ensalada de tomate')),
        ((SELECT idItemReporte FROM itemreporte ir
                      JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
                      WHERE rs.fechaInicioSemana = '2024-12-01'),
        (SELECT idVianda FROM vianda v WHERE v.descripcion = 'pancho con coca'));

INSERT INTO itemreporteviandasretiradasporheladera(id_item_reporte, heladera_idHeladera)
 VALUES ((SELECT idItemReporte FROM itemreporte ir
    JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
    WHERE rs.fechaInicioSemana = '2024-12-01'),
 (SELECT idHeladera FROM heladera WHERE nombre = 'MedranoUTN' and fechaInicioFuncionamiento = '2022-01-10')
 );
INSERT INTO itemreporteviandasretiradasporheladeraxvianda(ItemReporteViandasRetiradasPorHeladera_id_item_reporte, viandasRetiradas_idVianda)
VALUES ((SELECT idItemReporte FROM itemreporte ir
            JOIN reportesdelasemana rs ON rs.idReportesDeLaSemana = ir.id_reporte_de_la_semana
            WHERE rs.fechaInicioSemana = '2024-12-01'),
        (SELECT idVianda FROM vianda v WHERE v.descripcion = 'pancho con coca'));


INSERT INTO ubicacion (latitud, longitud) VALUES(-34.6725928524, -58.4074856269);

INSERT INTO zona (nombreZona, radio, ubicacion_idUbicacion, id_rol_tecnico)
VALUES ('Valentin Alsina', '500',
    (SELECT idUbicacion FROM ubicacion WHERE latitud = -34.6725928524 and longitud = -58.4074856269),
    (SELECT idrol FROM tecnico WHERE cuil = '20212445773' and apellido = 'Smith'));


-- Obtener el próximo valor para colaborador
SET @next_id_colaborador = (SELECT next_val FROM rol_sequence);
UPDATE rol_sequence SET next_val = next_val + 1;

INSERT INTO colaborador (idrol,estaActivo, puntuaje) VALUES (@next_id_colaborador,1, 43.5);

INSERT INTO persona (documento_idDocumento, rol_idrol)
VALUES ((SELECT idDocumento FROM documento WHERE numeroDocumento = '43244566'),
    (SELECT idrol FROM colaborador WHERE puntuaje = 43.5 and estaActivo = 1));

INSERT INTO personajuridica (razonSocial, rubro, tipo, id_persona)
VALUES ('Bicies S.A.','compra venta de bicicletas', 'EMPRESA',
(SELECT idPersona FROM persona p
    JOIN documento d ON d.idDocumento = p.documento_idDocumento
    WHERE d.numeroDocumento = '43244566'));

INSERT INTO contribucion (fecha, colaborador_idrol)
VALUES ('2024-12-03', (SELECT idrol FROM colaborador WHERE puntuaje = 43.5 and estaActivo = 1));

INSERT INTO ofrecerproductos (id_contribucion)
VALUES ((SELECT id_contribucion FROM contribucion c
    WHERE c.fecha = '2024-12-03' and c.colaborador_idrol = (SELECT idrol FROM colaborador
        WHERE puntuaje = 43.5 and estaActivo = 1)));

INSERT INTO oferta(imagenURL,nombre, puntosNecesarios, id_contribucion )
VALUES ('/views/imagenes/bici.png', 'Bicicleta k153',9,
    (SELECT id_contribucion FROM contribucion c
        WHERE c.fecha = '2024-12-03'
            and c.colaborador_idrol = (SELECT idrol FROM colaborador
                WHERE puntuaje = 43.5 and estaActivo = 1)));

INSERT INTO mediodecontacto(discriminator, mail, id_persona)
VALUES('CorreoElectronico','francotomascallero@gmail.com', (SELECT idPersona FROM persona p
    JOIN documento d ON d.idDocumento = p.documento_idDocumento
    WHERE d.numeroDocumento = '21244577'));