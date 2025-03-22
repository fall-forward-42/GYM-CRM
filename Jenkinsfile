pipeline {
    agent any  // Run on any Jenkins agent

    environment {
        IMAGE_NAME = 'lehaitien/gym-crm'
        IMAGE_TAG  = 'jenkins'
    }

    stages {
        stage('Build and Deploy on Remote Server') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS'),
                    file(credentialsId: 'gym-crm-env-file', variable: 'ENV_FILE')
                ]) {
                    sshagent(['my-ssh-key']) {
                        sh """
# Copy .env file lên server
scp -o StrictHostKeyChecking=no \$ENV_FILE root@192.168.1.101:~/gym-crm.env

ssh -o StrictHostKeyChecking=no root@192.168.1.101 << 'EOF'
echo "Connected to remote server."
uptime

echo "Cloning repository..."
rm -rf ~/gym-crm
git clone https://github.com/fall-forward-42/GYM-CRM.git ~/gym-crm
cd ~/gym-crm

echo "Logging into Docker Hub..."
echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin

echo "Building Docker image..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

echo "Pushing Docker image to Docker Hub..."
docker push ${IMAGE_NAME}:${IMAGE_TAG}

echo "Pulling the latest Docker image..."
docker pull ${IMAGE_NAME}:${IMAGE_TAG}

echo "Stopping and removing old container if it exists..."
docker stop gym-crm-container || true
docker rm gym-crm-container || true

echo "======= Contents of .env file ======="
cat ~/gym-crm.env
echo "====================================="

echo "Starting the new Docker container..."
docker run -d \\
    --name gym-crm-container \\
    --network backend \\
    -p 8081:8080 \\
    --env-file ~/gym-crm.env \\
    ${IMAGE_NAME}:${IMAGE_TAG}

echo "Docker container is running:"
docker ps | grep gym-crm-container

echo "======= Container Environment Variables ======="
docker exec gym-crm-container printenv | grep -E 'DB_|SPRING_|MINIO_|SERVER_PORT'

echo "Deployment completed successfully."
EOF
                        """
                    }
                }
            }
        }
    }
}
