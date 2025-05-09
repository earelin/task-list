terraform {
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.36.0"
    }
  }
}

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
          image = "mongo:8.0.9"
          name  = "mongo"
          env {
            name  = "MONGO_INITDB_ROOT_USERNAME"
            value = "root"
          }
          env {
            name  = "MONGO_INITDB_ROOT_PASSWORD"
            value = "secret"
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
            name  = "ME_CONFIG_MONGODB_ADMINUSERNAME"
            value = "root"
          }
          env {
            name  = "ME_CONFIG_MONGODB_ADMINPASSWORD"
            value = "secret"
          }
          env {
            name  = "ME_CONFIG_MONGODB_SERVER"
            value = "localhost"
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
      app = "mongo-express"
    }
    port {
      port        = 27017
      target_port = 27017
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
