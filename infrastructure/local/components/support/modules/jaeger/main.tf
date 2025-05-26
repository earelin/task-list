resource "kubernetes_stateful_set_v1" "jaeger" {
  metadata {
    name      = "jaeger"
    namespace = var.namespace
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "jaeger"
      }
    }
    template {
      metadata {
        labels = {
          app = "jaeger"
        }
      }
      spec {
        container {
          image = "jaegertracing/jaeger:${local.jaeger_version}"
          name  = "jeger"
          env {
            name  = "COLLECTOR_OTLP_ENABLED"
            value = "true"
          }
          env {
            name  = "SPAN_STORAGE_TYPE"
            value = "memory"
          }
          port {
            container_port = 4317
          }
          port {
            container_port = 4318
          }
          port {
            container_port = 16686
          }
        }
      }
    }
    service_name = "jaeger"
  }
}

resource "kubernetes_service_v1" "jaeger" {
  metadata {
    name      = "jaeger"
    namespace = var.namespace
  }
  spec {
    selector = {
      app = "jaeger"
    }
    port {
      name        = "jaeger-collector"
      port        = 4317
      target_port = 4317
    }
    port {
      name        = "jaeger-collector-grpc"
      port        = 4318
      target_port = 4318
    }
    port {
      name        = "jaeger-ui"
      port        = 16686
      target_port = 16686
    }
    type = "NodePort"
  }
}
