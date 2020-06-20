# Prometheus

To setup Prometheus 

kubectl apply -f prometheus-setup.yaml

If you have not setup HAProxy use the below

kubectl apply -f service-prometheus-grafana.yaml

Prometheus is now accessible at http://localhost:30000

username: admin  
password: prom-operator  

If you setup HAProxy you can access Prometheus from /grafana.