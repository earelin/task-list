resource "kubernetes_stateful_set_v1" "mongodb" {
  metadata {
    name      = "mongodb"
    namespace = var.namespace
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "mongo"
      }
    }
    template {
      metadata {
        labels = {
          app = "mongo"
        }
      }
      spec {
        container {
          image = "mongo:${local.mongodb_version}"
          name  = "mongo"
          env {
            name = "MONGO_INITDB_ROOT_USERNAME"
            value = "root"
          }
          env {
            name = "MONGO_INITDB_ROOT_PASSWORD"
            value = random_password.mongodb-password.result
          }
          port {
            container_port = 27017
          }
          volume_mount {
            mount_path = "/data/db"
            name       = "mongodb-data"
          }
        }

        container {
          image = "mongo-express:1.0.2-20-alpine3.19"
          name  = "mongo-express"
          env {
            name = "ME_CONFIG_MONGODB_ADMINUSERNAME"
            value = "root"
          }
          env {
            name = "ME_CONFIG_MONGODB_ADMINPASSWORD"
            value = random_password.mongodb-password.result
          }
          env {
            name  = "ME_CONFIG_MONGODB_SERVER"
            value = "localhost"
          }
          env {
            name  = "ME_CONFIG_BASICAUTH"
            value = "false"
          }
          port {
            container_port = 8081
          }
        }

        volume {
          name = "mongodb-data"
          persistent_volume_claim {
            claim_name = kubernetes_persistent_volume_claim_v1.mongodb.metadata[0].name
          }
        }
      }
    }
    service_name = "mongodb"
  }
}

resource "kubernetes_service_v1" "mongodb" {
  metadata {
    name      = "mongodb"
    namespace = var.namespace
  }
  spec {
    selector = {
      app = "mongodb"
    }
    port {
      name        = "mongodb"
      port        = 27017
      target_port = 27017
    }
    port {
      name        = "mongo-express"
      port        = 8081
      target_port = 8081
    }
    type = "NodePort"
  }
}

resource "kubernetes_persistent_volume_claim_v1" "mongodb" {
  metadata {
    name      = "mongodb-data-pvc"
    namespace = var.namespace
  }
  spec {
    access_modes = ["ReadWriteOnce"]
    resources {
      requests = {
        storage = "2Gi"
      }
    }
    storage_class_name = var.storage-class
  }
}

resource "random_password" "mongodb-password" {
  length  = 20
  special = false
  upper   = true
  lower   = true
  numeric = true
}
