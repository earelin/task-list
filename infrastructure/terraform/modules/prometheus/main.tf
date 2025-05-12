resource "kubernetes_stateful_set_v1" "prometheus" {
  metadata {
    name      = "prometheus"
    namespace = var.namespace
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "prometheus"
      }
    }
    template {
      metadata {
        labels = {
          app = "prometheus"
        }
      }
      spec {
        container {
          image = "prom/prometheus:${local.prometheus_version}"
          name  = "prometheus"
          port {
            container_port = 9090
          }
        }

        container {
          image = "grafana/grafana:${local.grafana_version}"
          name  = "grafana"
          port {
            container_port = 3000
          }
        }
      }
    }
    service_name = "prometheus"
  }
}

resource "kubernetes_service_v1" "prometheus" {
  metadata {
    name      = "prometheus"
    namespace = var.namespace
  }
  spec {
    selector = {
      app = "prometheus"
    }
    port {
      name        = "prometheus"
      port        = 9090
      target_port = 9090
    }
    port {
      name        = "grafana"
      port        = 3000
      target_port = 3000
    }
    type = "NodePort"
  }
}
