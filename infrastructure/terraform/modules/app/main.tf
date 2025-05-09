terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.5.0"
    }
  }
}

resource "docker_image" "task-list" {
  name = "task-list"
  build {
    context = "../../app"
    tag     = ["task-list:latest"]
  }
}
