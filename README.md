# DDS_Grupo3_2024_TPA

## Observabilidad 

### Puertos
- prometeus: puerto 7080
- grafana: puerto 3000
- prometeus query explorer: puerto 9090 

### Iniciar Prometheus
1. Descargar Prometeus
2. configurar el archivo "prometheus.yml" con
```yml
global:
  scrape_interval: 15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
scrape_configs:
  - job_name: "javalin"
    scrape_interval: 1s

    static_configs:
      - targets: ["localhost:7080"]
        labels:
            group: 'app'
`````
3. ejecutar prometheus por terminal con: " .\prometheus.exe --config.file=prometheus.yml"

``Nota: antes descargar prometheus e iniciar el TP``

### Usar grafana
1. ir a http://localhost:3000
2. Iniciar seccion username: "admin"  pass:"admin" (despues te pide crear una cuenta)
2. Connections > Data sources
3. click en Add data source
4. en Connection pones la url "http://localhost:9090"
5. click en save and test
6. Luego ir a Explore y usar


``Nota: antes descargar grafana e iniciar Prometheus``
### Metricas
- <b>http_server_requests_seconds_count</b> : cantidad de requests a ese endpoint
- <b>http_server_requests_seconds_sum</b> : suma de segundos en ejecutar todos los request a ese endpoint
- <b>http_server_requests_seconds_max</b> : duración en segundos, que tardo en ejecutar el request que mas tardó en ese endpoint