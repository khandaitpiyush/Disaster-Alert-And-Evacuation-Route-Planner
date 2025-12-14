
document.addEventListener('DOMContentLoaded', () => {

    const placeholder = document.createElement('div');
    placeholder.id = 'loader-placeholder';

    document.body.prepend(placeholder);

    fetch('loader.html')
        .then(response => {
            if (!response.ok) {
                throw new Error('Could not find loader.html');
            }
            return response.text(); 
        })
        .then(data => {
            placeholder.innerHTML = data;
        })
        .catch(error => {
            console.error('Error fetching the loader:', error);
            placeholder.innerHTML = '<p style="text-align: center;">Error loading component.</p>';
        });
});

window.addEventListener('load', () => {

    setTimeout(() => {
        const loaderWrapper = document.querySelector('.ring-loader-wrapper');

        if (loaderWrapper) {
            loaderWrapper.classList.add('hidden');
        }
    }, 200); 
});