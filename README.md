EOS Java Client

本项目参考自 bigbearbro/eos-client

*修改了些bug

*添加了新的功能

示例如下

        String url = "http://192.168.169.202:8888";
        //url="http://jungle2.cryptolions.io";
        
        EosClient eosClient = new EosClient(url);

        try {


            ///////////////通过私钥获取公钥//////////////////////////////
            //System.out.println(EccTool.privateToPublic("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvCD3"));
            //System.out.println(EosKeyUtils.getPublicKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvCD3"));


            /////////////////获取账户信息//////////////////////////////
            //GetAccountResults r = eosClient.getAccount("lifeeostoken");
            //System.out.println(JSON.toJSONString(r));

            ///////////////获取发币信息//////////////////////////////
            System.out.println(eosClient.getCurrencyStats("eoseggtoken1","EGG"));

            ///////////////获取账户余额//////////////////////////////
            System.out.println(eosClient.getCurrencyBalance("eostest11111","eosio.token","EOS"));

            PushTransactionResults pushTransactionResults = null;

            ///////////////创建账户/////////////////////////////////
            /*pushTransactionResults = eosClient.createAccount("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvCD3"
                    ,"eosio","user1234","EOS6w8em36iMnZHnL5Q9yJBc4tSJ8JrXgUnxxT5roau62Kqfou6GX","EOS6w8em36iMnZHnL5Q9yJBc4tSJ8JrXgUnxxT5roau62Kqfou6GX"
                    ,8164l, "0.1000 EOS","0.1000 EOS", 0l);
            System.out.println("创建成功 = " + pushTransactionResults.getTransactionId()+" \n ");*/

            //////////////////转账////////////////////////////////////////
            /*pushTransactionResults = eosClient.transfer("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvCD3","eosio.token", "eostest11111","eostest11112", "1.0000 EOS", "te1w");
            System.out.println("转账成功 = " + pushTransactionResults.getTransactionId()+" \n ");
            GetTransactionResult tr = eosClient.getTransaction(pushTransactionResults.getTransactionId());

            System.out.println(JSON.toJSONString(pushTransactionResults));*/

            
            ///////////////////修改权限key/////////////////////////////////
            pushTransactionResults = eosClient.updateUserAuthKey("5HvBzwH533cfpA2gFt8a16S8ZanGv9BASuZ2fgcTCD6ASs8ktUY","user1", "active","EOS7vqTxhFiXJopw4v5ED8C2URygkFusLHdnDujCgVvrRZPufqgYH");
            //pushTransactionResults = eosClient.updateUserAuthKey("5HvBzwH533cfpA2gFt8a16S8ZanGv9BASuZ2fgcTCD6ASs8ktUY","user1", "owner","EOS7vqTxhFiXJopw4v5ED8C2URygkFusLHdnDujCgVvrRZPufqgYH");
            System.out.println("变更成功 = " + pushTransactionResults.getTransactionId()+" \n ");

            ///////////////合约调用测试/////////////////////////////////
            //合约账号为egggamedice1
            //合约方法
            ////@abi action
            ///void offerbet(const account_name player, const asset& bet, const uint8_t roll_type, const uint64_t roll_border, string memo)
            
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
            
           /*
           	 * 获取合约执行权限
           	 * 示例：cleos set account permission user active '{"threshold": 1,"keys": [{"key": "EOS6MRkAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV","weight": 1}],
           	 * "accounts": [{"permission":{"actor":"hello","permission":"eosio.code"},"weight":1}]}' owner -p user
           	 * getEosioCodeAuth("hello","user","EOS6MRkAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV")
           	 */
           	/*private Action getEosioCodeAuth(String contractAccount, String actor, String publicKey) {
           		// auth
           		UpdateAuthActionData data = new UpdateAuthActionData();
           		data.setAccount(actor);
           		data.setPermission("active");
           		data.setParent("owner");
           
           		Authority authority = new Authority();
           		authority.setThreshold(1l);
           
           		List<PermissionLevelWeight> al = new ArrayList<PermissionLevelWeight>();
           
           		PermissionLevelWeight permissionLevelWeight = new PermissionLevelWeight();
           		permissionLevelWeight.setWeight(1l);
           
           		PermissionLevel permissionLevel = new PermissionLevel();
           		permissionLevel.setActor(contractAccount);
           		permissionLevel.setPermission("eosio.code");
           
           		permissionLevelWeight.setPermission(permissionLevel);
           		al.add(permissionLevelWeight);
           		authority.setAccounts(al);
           
           		List<KeyWeight> keys = new ArrayList<>();
           		KeyWeight keyWeight = new KeyWeight();
           		keyWeight.setWeight(1l);
           		keyWeight.setKey(publicKey);
           		keys.add(keyWeight);
           		authority.setKeys(keys);
           
           		List<WaitWeight> waitWeights = new ArrayList<>();
           		authority.setWaits(waitWeights);
           
           		data.setAuth(authority);
           		// action
           		Action authAction = new Action(actor, "eosio", "updateauth", data);
           		return authAction;
           	}
           
           */


            pushTransactionResults = eosClient.commonAction("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvCD3",
                    actions,null);
            List<ActionTrace> jj = pushTransactionResults.getProcessed().getActionTraces();
            if(jj.size()>0){
                System.out.println(jj.get(0).getConsole());
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
