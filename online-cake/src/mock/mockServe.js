import Mock from 'mockjs'
import banner from './banner.json'


Mock.mock('/mock/banner', 'get',
  () => banner
)