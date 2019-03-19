

import action.OfferBetActionData;
import pe.freeopen.eosclient.EosClient;
import pe.freeopen.eosclient.api.result.PushTransactionResults;
import pe.freeopen.eosclient.client.pack.AssetQuantity;
import pe.freeopen.eosclient.eosio.chain.action.Action;
import pe.freeopen.eosclient.eosio.chain.trace.ActionTrace;
import pe.freeopen.eosclient.utils.EosUtil;

import java.util.*;

/**
 * 测试
 *
 * @Author: ChenMingHao
 * @Date: 2018-11-26 14:24
 */
public class Test {

    public static void main(String[] args){


        String url = "http://192.168.169.202:8888";
        //url="http://jungle2.cryptolions.io";
        EosClient eosClient = new EosClient(url);

        try {


            ///////////////通过私钥获取公钥//////////////////////////////
            //System.out.println(EccTool.privateToPublic("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3"));
            //System.out.println(EosKeyUtils.getPublicKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3"));


            /////////////////获取账户信息//////////////////////////////
            //GetAccountResults r = eosClient.getAccount("lifeeostoken");
            //System.out.println(JSON.toJSONString(r));

            ///////////////获取发币信息//////////////////////////////
            //System.out.println(eosClient.getCurrencyStats("eoseggtoken1","EGG"));

            ///////////////获取账户余额//////////////////////////////
            //System.out.println(eosClient.getCurrencyBalance("eostest11111","eosio.token","EOS"));

            PushTransactionResults pushTransactionResults = null;

            ///////////////创建账户/////////////////////////////////
            /*pushTransactionResults = eosClient.createAccount("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3"
                    ,"eosio","user1234","EOS6w8em36iMnZHnL5Q9yJBc4tSJ8JrXgUnxxT5roau62Kqfou6GX","EOS6w8em36iMnZHnL5Q9yJBc4tSJ8JrXgUnxxT5roau62Kqfou6GX"
                    ,8164l, "0.1000 EOS","0.1000 EOS", 0l);
            System.out.println("创建成功 = " + pushTransactionResults.getTransactionId()+" \n ");*/

            //////////////////转账////////////////////////////////////////
            /*pushTransactionResults = eosClient.transfer("5KcFymdokNccLCZtKbJdoduWfyWThEpLqxsz1zVezrrAWNTF1N3","eosio.token", "eostest11111","eostest11112", "1.0000 EOS", "te1w");
            System.out.println("转账成功 = " + pushTransactionResults.getTransactionId()+" \n ");
            GetTransactionResult tr = eosClient.getTransaction(pushTransactionResults.getTransactionId());

            System.out.println(JSON.toJSONString(pushTransactionResults));*/

            ///////////////////修改active公钥///////////////

            pushTransactionResults = eosClient.updateUserAuthKey("5HvBzwH533cfpA2gFt8a16S8ZanGv9BASuZ2fgcTCD6ASs8ktUY","user1", "active","EOS7vqTxhFiXJopw4v5ED8C2URygkFusLHdnDujCgVvrRZPufqgYH");
            System.out.println("变更成功 = " + pushTransactionResults.getTransactionId()+" \n ");


            ///////////////合约调用测试/////////////////////////////////

            //下注action
            OfferBetActionData offerBetActionData = new OfferBetActionData();
            offerBetActionData.setPlayer("eostest11111");
            offerBetActionData.setBet(AssetQuantity.parse("1.0000 EGG", "eoseggtoken1"));
            offerBetActionData.setRollType(1L);
            offerBetActionData.setRollBorder(56L);
            offerBetActionData.setMemo("string");

            List<Action> actions = new ArrayList<>();
            Action action = new Action("eostest11111", "egggamedice1", "offerbet", offerBetActionData);
            actions.add(action);
            //可以添加多个action,符合事务性


            pushTransactionResults = eosClient.commonAction("5KcFymdokNccLCZtKbJdoduWfyWThEpLqxsz1zVezrrAWNTF1N3",
                    actions,null);
            List<ActionTrace> jj = pushTransactionResults.getProcessed().getActionTraces();
            if(jj.size()>0){
                System.out.println(jj.get(0).getConsole());
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }



    }

}
