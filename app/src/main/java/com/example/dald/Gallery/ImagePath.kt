package com.example.dald.Gallery

/**
 * Class which stores the image path and image name
 */
class ImagePath {
    var imagePath: String? = null
    var imageName: String? = null

    constructor(imagePath: String?, imageName: String?) {
        this.imagePath = imagePath
        this.imageName = imageName
    }

    constructor()
}