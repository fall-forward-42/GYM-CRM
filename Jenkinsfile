pipeline {
    agent any  // Chạy trên bất kỳ agent nào

    environment {
        RUN_CLONE = 'false'  // đổi thành 'true' nếu cần clone repo
    }

    stages {
        stage('Clone Repository') {
            when {
                expression { RUN_CLONE == 'true' }
            }
            steps {
                script {
                    sh '''
                        rm -rf repo
                        git clone https://github.com/fall-forward-42/GYM-CRM.git repo
                    '''
                }
            }
        }

        stage('Deploy to Server with Credential') {
            steps {
                withCredentials([
                    string(credentialsId: 'DB_MSQL_USERNAME', variable: 'DB_USER'),
                    string(credentialsId: 'DB_MSQL_PASSWORD', variable: 'DB_PASS')
                ]) {
                    sshagent(['my-ssh-key']) {
                        sh """
ssh -o StrictHostKeyChecking=no root@192.168.1.101 << 'EOF'
echo "Đã kết nối SSH vào server!"
uptime

echo "Pulling latest Docker image..."
docker pull lehaitien/gym-crm:latest

echo "Checking and removing old container (if exists)..."
docker stop gym-crm-container || true
docker rm gym-crm-container || true

echo "Creating .env file..."
cat > ~/gym-crm.env <<EOL
DB_MSQL_USERNAME=${DB_USER}
DB_MSQL_PASSWORD=${DB_PASS}
EOL

echo ".env file created:"
cat ~/gym-crm.env

echo "Running new Docker container with .env file..."
docker run -d \\
    --name gym-crm-container \\
    --network backend \\
    -p 8081:8080 \\
    --env-file ~/gym-crm.env \\
    lehaitien/gym-crm:latest

echo "Docker container running:"
docker ps | grep gym-crm-container

echo "Hoàn thành!"
EOF
                """
                    }
                }
            }
        }
    }
}
