package cn.itcast.feign.clients;


import cn.itcast.feign.common.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "userservice")
public interface ShippingOrderClient {
//
//    @PostMapping("/user/{id}")
//    void findById(@PathVariable("id") Long id);
//
//    /**
//     * 修改某个订单的信息
//     * */
//    @PutMapping("/update-order-by-id")
//    public R updateOrderById(@RequestBody ShippingOrder shippingOrder);
//
//    /**
//     * 更新订单表涉及订单状态
//     * */
//    @PostMapping("/update-orderState-by-transitId")
//    public void updateOrderStateByInTransitId(@RequestParam Integer inTransitId, @RequestParam Integer state);

}
