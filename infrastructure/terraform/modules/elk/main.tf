resource "kubernetes_stateful_set_v1" "elk" {
  metadata {
    name      = "elk"
    namespace = var.namespace
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "elk"
      }
    }
    template {
      metadata {
        labels = {
          app = "elk"
        }
      }
      spec {
        container {
          image = "elasticsearch:${local.elk_version}"
          name  = "elasticsearch"
          port {
            container_port = 9200
          }
        }

        container {
          image = "kibana:${local.elk_version}"
          name  = "kibana"
          port {
            container_port = 5601
          }
        }
      }
    }
    service_name = "elk"
  }
}

resource "kubernetes_service_v1" "elk" {
  metadata {
    name      = "elk"
    namespace = var.namespace
  }
  spec {
    selector = {
      app = "elk"
    }
    port {
      name        = "elasticsearch"
      port        = 9200
      target_port = 9200
    }
    port {
      name        = "kibana"
      port        = 5601
      target_port = 5601
    }
    type = "NodePort"
  }
}
