new Vue({
    el: '#app',
    data: {
        sidebarOpen: false,
        navbarShadow: false
    },
    computed: {
        sidebarStyle() {
            return {
                left: this.sidebarOpen ? '0px' : '-250px'
            };
        },
        overlayStyle() {
            return {
                display: this.sidebarOpen ? 'block' : 'none'
            };
        },
        mainContentClass() {
            return {
                'main-content': true,
                'main-content-shifted': this.sidebarOpen
            };
        },
        navbarClass() {
            return {
                'custom-navbar': true,
                'custom-navbar-shadow': this.navbarShadow,
                'custom-navbar-border': !this.navbarShadow
            };
        }
    },
    methods: {
        toggleSidebar() {
            this.sidebarOpen = !this.sidebarOpen;
            setTimeout(initMasonry, 350);
        },
        closeSidebar() {
            this.sidebarOpen = false;
            setTimeout(initMasonry, 350);
        },
        handleScroll() {
            this.navbarShadow = window.scrollY > 0;
        }
    },
    mounted() {
        window.addEventListener('scroll', this.handleScroll);
    },
    beforeDestroy() {
        window.removeEventListener('scroll', this.handleScroll);
    }
});