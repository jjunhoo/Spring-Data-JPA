package study.datajpa.controller;

import static org.springframework.util.ObjectUtils.isEmpty;

import lombok.Getter;
import lombok.Setter;

public abstract class Verifier {
    abstract void verify(WRequestHttpServletRequestWrapper request) throws Exception;

    private final static String PARTNER_HEADER_NAME = "partnerId";

    Seller getSeller(String sellerId, SellerType sellerType, WRequestHttpServletRequestWrapper request) {
        Seller seller = new Seller();
        if (SellerType.GROUP == sellerType) {
            seller.setGroupId(sellerId);
            String partnerId = request.getHeader(PARTNER_HEADER_NAME);
            if (isEmpty(partnerId)) {
                // throw new PartnerIdNotFoundException(ERROR_CODE_2004);
                System.out.println("partnerId 정보가 없습니다.");
            }
            seller.setPartnerId(partnerId);
        } else {
            seller.setPartnerId(sellerId);
        }

        return seller;
    }

    @Getter
    @Setter
    class Seller {
        String groupId;
        String partnerId;
    }
}
