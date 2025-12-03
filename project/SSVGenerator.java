//import jade.core.Agent;
//
//public class SSVGenerator extends Agent
//{
//    private double epsilon;
//    private double delta;
//
//    protected void setup()
//    {
//        System.out.println("SSVGenerator starting");
//        Object[] args = getArguments();
//        if(args.length != 2)
//        {
//            doDelete();
//        }
//
//        this.epsilon =  Double.parseDouble(args[0].toString());
//        this.delta =  Double.parseDouble(args[1].toString());
//
//        if(epsilon < 0 || epsilon > 1 || delta < 0 || delta > 1)
//        {
//            doDelete();
//        }
//
//        System.out.println(this.epsilon);
//        System.out.println(this.delta);
//    }
//}
