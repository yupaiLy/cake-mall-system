import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import styleImport from 'vite-plugin-style-import'
// https://vitejs.dev/config/
export default defineConfig({
	resolve: {
		alias: {
			'@': resolve(__dirname, 'src') // 设置 `@` 指向 `src` 目录
		}
	},
	css: {
		// css预处理器
		preprocessorOptions: {
			less: {
				charset: false,
				additionalData: '@import "./src/assets/styles/common.less";'
			}
		}
	},
	plugins: [
		vue(),
		styleImport({
			libs: [
				{
					libraryName: 'element-plus',
					esModule: true,
					resolveStyle: name => {
						return `element-plus/lib/theme-chalk/${name}.css`
					},
					resolveComponent: name => {
						return `element-plus/lib/${name}`
					}
				}
			]
		})
	]
})
