
import java.util.ArrayList;
import java.util.List;

/**
 * Team Lead Class
 *
 * Work on: Conor
 */

public class TeamLead extends Developer {
    private SoftwareProjectManager manager;
    private TeamLead teamLead;
    private ArrayList<Developer> developers;

    public TeamLead(SoftwareProjectManager manager, Firm firm, List<Developer> developers) {
        this.manager = manager;
    }
}
