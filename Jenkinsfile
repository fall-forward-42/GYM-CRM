pipeline {
    agent any  // Chạy trên bất kỳ agent nào

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    // Xóa thư mục cũ nếu có (tránh lỗi)
                    sh 'rm -rf repo'
                    
                    // Clone repository về thư mục 'repo'
                    sh 'git clone https://github.com/fall-forward-42/GYM-CRM.git repo'
                }
            }
        }
        stage('SSH to Server') {
            steps {
                sshagent(['my-ssh-key']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no root@192.168.1.101 << EOF
                            echo "Đã kết nối SSH vào server!"
                            uptime
                            echo "Kiểm tra Docker..."
                            docker ps
                            echo "Hoàn thành!"
                        EOF
                    '''
                }
            }
        }
    }
}
