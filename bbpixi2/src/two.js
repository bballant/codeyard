//Aliases
let Application = PIXI.Application,
    loader = PIXI.loader,
    resources = PIXI.loader.resources,
    Sprite = PIXI.Sprite;

// let width = 320;
// let height = 240;
let width = 640;
let height = 480;

//Create a Pixi Application
let app = new Application({
    width: width,
    height: height,
    antialias: true,
    transparent: false,
    resolution: 1
  }
);

//Add the canvas that Pixi automatically created for you to the HTML document
document.body.appendChild(app.view);

//load an image and run the `setup` function when it's done
loader
    .add("images/bottom.gif")
    .add("images/bottom_left.gif")
    .add("images/bottom_right.gif")
    .add("images/cat.png")
    .add("images/ground.gif")
    .add("images/left.gif")
    .add("images/right.gif")
    .add("images/rock1.png")
    .add("images/rock2.png")
    .add("images/rock3.gif")
    .add("images/rock4.gif")
    .add("images/rock_tile.gif")
    .add("images/top.gif")
    .add("images/top_left.gif")
    .add("images/top_right.gif")
    .load(setup);

function cloneAndPlace(texture, x, y) {
    const sprite = new Sprite(texture);
    sprite.position.set(x,y);
    app.stage.addChild(sprite);
}

//This `setup` function will run when the image has loaded
function setup() {


    //Create the cat sprite
    let cat         = resources["images/cat.png"].texture;
    let bottom      = resources["images/bottom.gif"].texture;
    let bottomLeft  = resources["images/bottom_left.gif"].texture;
    let bottomRight = resources["images/bottom_right.gif"].texture;
    let ground      = resources["images/ground.gif"].texture;
    let left        = resources["images/left.gif"].texture;
    let right       = resources["images/right.gif"].texture;
    let rock1       = resources["images/rock1.png"].texture;
    let rock2       = resources["images/rock2.png"].texture;
    let rock3       = resources["images/rock3.gif"].texture;
    let rock4       = resources["images/rock4.gif"].texture;
    let rockTile    = resources["images/rock_tile.gif"].texture;
    let top         = resources["images/top.gif"].texture;
    let topLeft     = resources["images/top_left.gif"].texture;
    let topRight    = resources["images/top_right.gif"].texture;

    cloneAndPlace(topLeft,0,0);

    for (x = 40; x <= (width - 80); x = x + 40) {
        cloneAndPlace(top,x,0);
    }

    cloneAndPlace(topRight,width - 40,0);

    for (y = 40; y <= (height - 80); y = y + 40) {

        cloneAndPlace(left,0,y);

        for (x = 40; x <= (width - 80); x = x + 40) {
            cloneAndPlace(rockTile,x,y);
        }

        cloneAndPlace(right,width-40, y);
    }

    cloneAndPlace(bottomLeft,0,height -40);

    for (x = 40; x <= (width - 80); x = x + 40) {
        cloneAndPlace(bottom,x,height-40);
    }

    cloneAndPlace(bottomRight,width-40,height-40);

    cloneAndPlace(rock1, 85, 58);
    cloneAndPlace(rock2, 205, 108);
    cloneAndPlace(rock3, 45, 78);
    cloneAndPlace(rock4, 185, 148);
}
