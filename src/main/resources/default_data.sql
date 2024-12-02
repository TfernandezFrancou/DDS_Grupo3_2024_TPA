
INSERT INTO direccion (altura, localidad, nombreCalle)
VALUES ('255', 'CABA', 'Medrano');
INSERT INTO heladera (capacidadEnViandas, fechaInicioFuncionamiento, nombre, temperaturaActualHeladera, direccion_idDireccion)
VALUES (123, '2022-01-10', 'MedranoUTN', 12,
    (SELECT idDireccion
     FROM direccion
     WHERE nombreCalle = 'Medrano' AND localidad = 'CABA'));
