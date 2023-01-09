
export const compressPic = (file, encoderOtp = 0.3, minSize = 300 * 1024) => {
  return new Promise(resolve => {
    // 1. 通过FileReader读取文件
    const reader = new FileReader()
    let res = reader.readAsDataURL(file)
    reader.onload = (event) => {
      // 2. 读取完毕之后获取图片的base64(上文伏笔),并创建新图片
      const { result: src } = event.target
      console.log(src.length)
      if (src.length < minSize) {
        return resolve(file)
      }
      const image = new Image()
      image.src = src
      image.onload = () => {
        // 3.图片加载完之后通过canvas压缩图片
        const canvas = document.createElement('canvas')
        canvas.width = image.width
        canvas.height = image.height
        // 3.1 绘制canvas
        const ctx = canvas.getContext('2d')
        ctx.drawImage(image, 0, 0, image.width, image.height)
        // 3.2 返回图片url地址,并且进行压缩
        const canvasURL = canvas.toDataURL(file.type, encoderOtp)
        const buffer = atob(canvasURL.split(',')[1])
        // 3.3 bufferArray 无符号位字节数组 相当于在内存中开辟length长度的字节空间
        let length = buffer.length
        const bufferArray = new Uint8Array(length)
        // 3.4 给新开辟的bufferArray赋值
        while (length--) {
          bufferArray[length] = buffer.charCodeAt(length)
        }
        // 3.5将压缩后的文件通过resolve返回出去
        const resultFile = new File([bufferArray], file.name, { type: file.type })
        console.log(resultFile)
        resolve(resultFile)
      }
    }
  })
}
