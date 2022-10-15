

function updateQueryParam(name, value) {
    if ('URLSearchParams' in window) {
        const searchParams = new URLSearchParams(window.location.search);
        searchParams.set(name, value);
        window.location.search = searchParams.toString();
    }
}