package mech_design;

/**
 *
 * @author Aravind S R
 */
public class mdInitializer
{
    /**
     * Given a mdGameObject and a filename, this function will initialize the complete mdGameObject.
     * This is done by calling the corresponding initializers. For more information see documentation
     * of each of the initializers.
     */

    static void init_md_gameobj(mdGameObject mdobj, String fname)
    {
        mdHeaderInit.mdHeadInit(mdobj, fname);
        mdUtilityInit.mdUtilInit(mdobj, fname);
        mdSCFinit.mdScfInit(mdobj, fname);
        mdTrueType.mdtruetypeInit(mdobj, fname);
        mdBeliefInit.mdbeliefInit(mdobj, fname);

    }
}
