var url = window.location.href;
var lang = getLanguage(url);
console.log(lang);

function getLanguage(url) {
    // if language is set via url parameter
    if (url.includes('?lang=')) {
        return url.split('?lang=')[1].split('&')[0];
    }
    // if language is set via url route
    else {
        return url.split('/')[1].split('?')[0];
    }
}
