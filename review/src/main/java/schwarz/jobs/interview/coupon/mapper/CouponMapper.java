package schwarz.jobs.interview.coupon.mapper;

import schwarz.jobs.interview.coupon.domain.CouponEntity;
import schwarz.jobs.interview.coupon.dto.CouponDTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    
    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);
    
    CouponEntity toEntity(CouponDTO request);
    
    CouponDTO toDTO(CouponEntity coupon);
    
    List<CouponDTO> toDTOList(List<CouponEntity> coupons);
}