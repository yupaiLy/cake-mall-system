function compressImg (file, quality) {
  if (file[0]) {
    return Promise.all(Array.from(file).map(e => compressImg(e, quality))) // 如果是 file 数组返回 Promise 数组
  } else {
    return new Promise((resolve) => {
      const reader = new FileReader() // 创建 FileReader
      reader.onload = ({ target: { result: src } }) => {
        const image = new Image() // 创建 img 元素
        image.onload = async () => {
          const canvas = document.createElement('canvas') // 创建 canvas 元素
          canvas.width = image.width
          canvas.height = image.height
          canvas.getContext('2d').drawImage(image, 0, 0, image.width, image.height) // 绘制 canvas
          const canvasURL = canvas.toDataURL(file.type, quality)
          const buffer = atob(canvasURL.split(',')[1])
          let length = buffer.length
          const bufferArray = new Uint8Array(length)
          console.log(bufferArray, new ArrayBuffer(length))
          while (length--) {
            bufferArray[length] = buffer.charCodeAt(length)
          }
          console.log([bufferArray])
          const miniFile = new File([bufferArray], file.name, {
            type: file.type
          })
          resolve({
            file: miniFile,
            origin: file,
            beforeSrc: src,
            afterSrc: canvasURL,
            beforeKB: Number((file.size / 1024).toFixed(2)),
            afterKB: Number((miniFile.size / 1024).toFixed(2))
          })
        }
        image.src = src
      }
      reader.readAsDataURL(file)
    })
  }
}