axios.defaults.baseURL = 'http://localhost:8080'

axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = token;
        }
        return config
    }
)