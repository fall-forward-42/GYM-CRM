pipeline {
    agent any  // Chạy trên bất kỳ agent nào

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    // Xóa thư mục cũ nếu có (tránh lỗi)
                    sh 'rm -rf repo'
                    
                    // Clone repository về thư mục 'repo'
                    sh 'git clone https://github.com/your-username/your-repo.git repo'
                }
            }
        }
    }
}
