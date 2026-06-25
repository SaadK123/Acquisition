import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MarketInvService {


    EntityManager entityManager;

    @Transactional
    public MarketInvestment findMarket(String marketId) {
        try {
          return entityManager.createQuery("select m from MarketInvestment m where m.id = :id"
                  ,MarketInvestment.class).setParameter(marketId,"id").getSingleResult();
        }catch (Exception _) {}
       return null;
    }

    @Transactional
    public List<MarketInvestment> getAllMarkets() {
        return entityManager.createQuery("SELECT m FROM MarketInvestment m",MarketInvestment.class).getResultList();
    }



    static Random rnd = new Random();
    @Scheduled(fixedRate = 15000)
    @Transactional
    public void reloadMarkets() {
        List<MarketInvestment> markets = getAllMarkets();

        for(MarketInvestment marketInvestment : markets) {
            double randomPriceVariation = rnd.nextDouble(1,100);
            if(!rnd.nextBoolean()) {
               randomPriceVariation = -randomPriceVariation;
            }
            final double newPrice = randomPriceVariation + marketInvestment.getCurrentPricePerStock();
            marketInvestment.setPrice(newPrice);
        }
    }


    public InvestmentReport getInvestmentReport(Investment investment) {
       double lastPreviousPrice =  investment.getMarketInvestment().getLastPrices().getLast();

       double currentPrice =  investment.getMarketInvestment().getCurrentPricePerStock();


       double currentStockHold = investment.getStockBought();


       double previousStockPower = lastPreviousPrice * currentStockHold,
               currentStockPower = currentPrice * currentStockHold;

       double netIncome = currentStockPower - previousStockPower;

       double growth = netIncome / previousStockPower * 100;

       return new InvestmentReport(investment.getId(),netIncome,previousStockPower,currentStockPower,growth);
    }
}
