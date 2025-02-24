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
                    string(credentialsId: 'DB_MSQL_USERNAME', variable: 'DB_USER'),
                    string(credentialsId: 'DB_MSQL_PASSWORD', variable: 'DB_PASS')
                ]) {
                    sshagent(['my-ssh-key']) {
                        sh """
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

echo "Creating .env file..."
cat <<EOL > ~/gym-crm.env
DB_MSQL_USERNAME=${DB_USER}
DB_MSQL_PASSWORD=${DB_PASS}
EOL

echo "Displaying .env file content:"
cat ~/gym-crm.env

echo "Starting the new Docker container..."
docker run -d \\
    --name gym-crm-container \\
    --network backend \\
    -p 8081:8080 \\
    --env-file ~/gym-crm.env \\
    ${IMAGE_NAME}:${IMAGE_TAG}

echo "Docker container is running:"
docker ps | grep gym-crm-container

echo "Deployment completed successfully."
EOF
                        """
                    }
                }
            }
        }
    }
}
