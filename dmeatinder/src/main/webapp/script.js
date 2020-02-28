/* LikeCarousel (c) 2019 Simone P.M. github.com/simonepm - Licensed MIT */

class Carousel {

    constructor(element) {

        this.i = 0;

        this.board = element;

        // add first two cards programmatically
        this.push();
        this.push();
        this.push();

        // handle gestures
        this.handle()
    }

    handle() {

        // list all cards
        this.cards = this.board.querySelectorAll('.card');

        // get top card
        this.topCard = this.cards[this.cards.length - 1];

        // get next card
        this.nextCard = this.cards[this.cards.length - 2];

        // if at least one card is present
        if (this.cards.length > 0) {

            // set default top card position and scale
            this.topCard.style.transform =
                'translateX(-50%) translateY(-50%) rotate(0deg) rotateY(0deg) scale(1)';

            // destroy previous Hammer instance, if present
            if (this.hammer) this.hammer.destroy();

            // listen for tap and pan gestures on top card
            this.hammer = new Hammer(this.topCard);
            this.hammer.add(new Hammer.Tap());
            this.hammer.add(new Hammer.Pan({
                position: Hammer.position_ALL, threshold: 0
            }));

            // pass events data to custom callbacks
            this.hammer.on('tap', (e) => {
                this.onTap(e)
            });
            this.hammer.on('pan', (e) => {
                this.onPan(e)
            })

        }

    }

    onTap(e) {
        // get finger position on top card
        let propX = (e.center.x - e.target.getBoundingClientRect().left) / e.target.clientWidth;

        // get degree of Y rotation (+/-15 degrees)
        let rotateY = 15 * (propX < 0.05 ? -1 : 1);

        // change the transition property
        this.topCard.style.transition = 'transform 100ms ease-out';

        // rotate
        this.topCard.style.transform =
            'translateX(-50%) translateY(-50%) rotate(0deg) rotateY(' + rotateY + 'deg) scale(1)';


        // wait transition end
        setTimeout(() => {
            // reset transform properties
            this.topCard.style.transform =
                'translateX(-50%) translateY(-50%) rotate(0deg) rotateY(0deg) scale(1)';
            this.topCard.classList.add("full")
        }, 100)

    }

    onPan(e) {

        if (!this.isPanning) {

            this.isPanning = true;

            // remove transition properties
            this.topCard.style.transition = null;
            if (this.nextCard) this.nextCard.style.transition = null;

            // get top card coordinates in pixels
            let style = window.getComputedStyle(this.topCard);
            let mx = style.transform.match(/^matrix\((.+)\)$/);
            this.startPosX = mx ? parseFloat(mx[1].split(', ')[4]) : 0;
            this.startPosY = mx ? parseFloat(mx[1].split(', ')[5]) : 0;

            // get top card bounds
            let bounds = this.topCard.getBoundingClientRect();

            // get finger position on top card, top (1) or bottom (-1)
            this.isDraggingFrom =
                (e.center.y - bounds.top) > this.topCard.clientHeight / 2 ? -1 : 1

        }

        // calculate new coordinates
        let posX = e.deltaX + this.startPosX;
        let posY = e.deltaY + this.startPosY;

        // get ratio between swiped pixels and the axes
        let propX = e.deltaX / this.board.clientWidth;
        let propY = e.deltaY / this.board.clientHeight;

        // get swipe direction, left (-1) or right (1)
        let dirX = e.deltaX < 0 ? -1 : 1;

        // calculate rotation, between 0 and +/- 45 deg
        let deg = this.isDraggingFrom * dirX * Math.abs(propX) * 45;

        // calculate scale ratio, between 95 and 100 %
        let scale = (95 + (5 * Math.abs(propX))) / 100;

        // move top card
        this.topCard.style.transform =
            'translateX(' + posX + 'px) translateY(' + posY + 'px) rotate(' + deg + 'deg) rotateY(0deg) scale(1)';

        // scale next card
        if (this.nextCard) this.nextCard.style.transform =
            'translateX(-50%) translateY(-50%) rotate(0deg) rotateY(0deg) scale(' + scale + ')';


        // check threshold
        if (propX > 0.2) {
            this.topCard.classList.remove("left");
            this.topCard.classList.remove("up");
            this.topCard.classList.add("right")
        } else if (propX < -0.2) {
            this.topCard.classList.remove("right");
            this.topCard.classList.remove("up");
            this.topCard.classList.add("left")
        } else if (propY < -0.2) {
            this.topCard.classList.remove("right");
            this.topCard.classList.remove("left");
            this.topCard.classList.add("up")
        } else {
            this.topCard.classList.remove("right");
            this.topCard.classList.remove("left");
            this.topCard.classList.remove("up")
        }


        if (e.isFinal) {

            this.isPanning = false;

            let successful = false;

            // set back transition properties
            this.topCard.style.transition = 'transform 200ms ease-out';
            if (this.nextCard) this.nextCard.style.transition = 'transform 100ms linear';


            // check threshold
            if (propX > 0.20) {

                axios.post('https://dmea.deluxxe.ch/api/like/' + this.topCard.id);

                successful = true;
                // get right border position
                posX = this.board.clientWidth

            } else if (propX < -0.20) {
                axios.delete('https://dmea.deluxxe.ch/api/dislike/' + this.topCard.id);

                successful = true;
                // get left border position
                posX = -(this.board.clientWidth + this.topCard.clientWidth)

            } else if (propY < -0.20) {
                axios.post('https://dmea.deluxxe.ch/api/superlike/' + this.topCard.id);

                successful = true;
                // get top border position
                posY = -(this.board.clientHeight + this.topCard.clientHeight)

            } else if (propY > 0.20) {
                this.topCard.classList.remove("full")
            }

            if (successful) {

                // throw card in the chosen direction
                this.topCard.style.transform =
                    'translateX(' + posX + 'px) translateY(' + posY + 'px) rotate(' + deg + 'deg)';

                // wait transition end
                setTimeout(() => {
                    // remove swiped card
                    this.board.removeChild(this.topCard);
                    // add new card
                    this.push();
                    // handle gestures on new top card
                    this.handle()
                }, 200)

            } else {

                // reset cards position
                this.topCard.style.transform =
                    'translateX(-50%) translateY(-50%) rotate(0deg) rotateY(0deg) scale(1)';
                if (this.nextCard) this.nextCard.style.transform =
                    'translateX(-50%) translateY(-50%) rotate(0deg) rotateY(0deg) scale(0.95)'

            }

        }

    }

    push() {

        let card = document.createElement('div');

        card.classList.add('card');
        card.id = openCards[this.i].id;

        if (openCards.length === 0){
            card.innerHTML = "<h1>Keine Karten mehr</h1>";
        } else {
            card.innerHTML = toHTML(openCards[this.i]);
        }
        this.i++;



        if (this.board.firstChild) {
            this.board.insertBefore(card, this.board.firstChild)
        } else {
            this.board.append(card)
        }

    }

}



function shuffle(a) {
    var j, x, i;
    for (i = a.length - 1; i > 0; i--) {
        j = Math.floor(Math.random() * (i + 1));
        x = a[i];
        a[i] = a[j];
        a[j] = x;
    }
    return a;
}

function toHTML(card) {
    let weekday = [
        'Sonntag',
        'Montag',
        'Dienstag',
        'Mittwoch',
        'Donnerstag',
        'Freitag',
        'Samstag'
    ];

    let startTime = moment.utc(card.startTime);
    let endTime = moment.utc(card.endTime);
    return "<h2>" + card.name + "</h2>" +
        "<i class=\"far fa-calendar-day\"></i> " + weekday[startTime.day()] + " " + startTime.date() + ". April <br>" +
        "<i class=\"far fa-clock\"></i> " + startTime.format("HH") + ":" + endTime.format("mm") + " - " + endTime.format("HH") + ":" + endTime.format("mm") + "" +
        "<div class='description'>" + card.description + "</div>" +
        "<b><i class=\"fas fa-map-marker\"></i> Standort:</b> " + card.place +
        "  <p><a href='" + card.externalLink + "'><i class=\"far fa-external-link\"></i> Mehr Informationen</a></p>";
}

let board = document.getElementById('board');
let overview = document.getElementById('overview');
let list = document.getElementsByClassName('list')[0];
let choose = document.getElementsByClassName('choose')[0];
let clear = document.getElementsByClassName('clear')[0];

list.onclick = function () {
    refresh(function () {

    });
    list.classList.add("active");
    choose.classList.remove("active");
    board.classList.add('hidden');
    overview.classList.remove('hidden');

};

choose.onclick = function () {
    list.classList.remove("active");
    choose.classList.add("active");
    board.classList.remove('hidden');
    overview.classList.add('hidden');
};

function showBoard(after){
    refresh(after)
}

function showOverview(){
    refresh()
}

clear.onclick = function(){
    axios.post('https://dmea.deluxxe.ch/api/clear');
};


let allcards = [];
let openCards = [];
let likedCards = [];
function refresh(after) {
    axios.get('https://dmea.deluxxe.ch/api/list')
        .then(function (response) {
            allcards = response.data;
            openCards = shuffle(allcards.filter(function (element) {
                return element.status === 'unrated'
            }));
            likedCards = shuffle(allcards.filter(function (element) {
                return element.status === 'unrated'
            }));
            if (after){
                after();
            }
        })
        .catch(function (error) {
            // handle error
            console.log(error);
        });
}

showBoard(function () {
    let board = document.querySelector('#board');
    let carousel = new Carousel(board)
});
