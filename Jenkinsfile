pipeline {
    agent any

    environment {
        IMAGE_NAME = 'lehaitien/gym-crm'
        IMAGE_TAG  = 'jenkins'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/fall-forward-42/GYM-CRM.git', branch: 'main'
            }
        }

        stage('Build Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                ]) {
                    sh '''
                        echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
                        docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                    '''
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                ]) {
                    sh '''
                        echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                    '''
                }
            }
        }

        stage('Copy .env file to Remote Server') {
            steps {
                withCredentials([
                    file(credentialsId: 'gym-crm-env-file', variable: 'ENV_FILE')
                ]) {
                    sshagent(['my-ssh-key']) {
                        sh '''
                            scp -o StrictHostKeyChecking=no $ENV_FILE root@192.168.1.101:~/gym-crm.env
                        '''
                    }
                }
            }
        }

        stage('Deploy on Remote Server') {
            steps {
                sshagent(['my-ssh-key']) {
                    sh '''
ssh -o StrictHostKeyChecking=no root@192.168.1.101 << 'EOF'

echo "Connected to remote server."
uptime

echo "Cloning repository..."
rm -rf ~/gym-crm
git clone https://github.com/fall-forward-42/GYM-CRM.git ~/gym-crm
cd ~/gym-crm

echo "Pulling the latest Docker image..."
docker pull lehaitien/gym-crm:jenkins

echo "Stopping and removing old container if it exists..."
docker stop gym-crm-container || true
docker rm gym-crm-container || true

echo "Starting the new Docker container..."
docker run -d \\
    --name gym-crm-container \\
    --network backend \\
    -p 8081:8080 \\
    --env-file ~/gym-crm.env \\
    lehaitien/gym-crm:jenkins

docker ps | grep gym-crm-container
echo "Deployment completed successfully."
EOF
                    '''
                }
            }
        }
    }
}
