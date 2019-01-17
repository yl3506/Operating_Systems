import java.io.*;
import java.util.*;

public class Linker{
	public static void main(String[] args){
		final int MACHINESIZE = 300;
		final int symbol_len = 8; // max number of characters in a symbol
		Scanner sc = new Scanner(System.in);
		int num_m = sc.nextInt();
		ArrayList<Module> all_module = new ArrayList<>();
		Hashtable<String, Var> var_table = new Hashtable<>();
		
		// first scan
		int cur_add = 0;
		for (int cur_module = 0; cur_module < num_m; cur_module++){
			Module cur_m = new Module(cur_module);
			// cur_m.set_mod_num(cur_module);
			// read def list
			int num_def = sc.nextInt();
			for (int j = 0; j < num_def; j++){
				String v = sc.next();
				int add = sc.nextInt();
				Var variable = new Var(v, (cur_add + add), cur_m);
				// check multiple define
				if(var_table.containsKey(v)){
					Var temp_var = var_table.get(v);
					temp_var.set_mult_def();
					temp_var.set_abs_add(cur_add + add);
					temp_var.set_mod(cur_m);
					continue;
				}
				var_table.put(v, variable);
			}
			// read use list
			int num_use = sc.nextInt();
			for (int j = 0; j < num_use; j++){
				String v = sc.next();
				ArrayList<Integer> use_list = new ArrayList<>();
				while(true){
					int cur_int = sc.nextInt();
					if (cur_int == -1){
						break;
					}
					use_list.add(cur_int);
					if(cur_m.check_mult_ref(cur_int) != null){
						cur_m.add_mult_ref(cur_int, 1);
					} else{
						cur_m.add_mult_ref(cur_int, 0);
					}
				}
				cur_m.add_use(v, use_list);	
			}
			// read prog text
			int num_text = sc.nextInt();
			for (int j = 0; j < num_text; j++){
				cur_m.add_text(sc.nextInt());
			}
			// config current module
			cur_m.set_start(cur_add);
			cur_m.set_len(num_text);
			all_module.add(cur_m);
			cur_add += num_text;
		}
		// check definition exceeds module size
		for (Enumeration<Var> all_var = var_table.elements(); all_var.hasMoreElements();){
			Var variable = all_var.nextElement();
			variable.check_var_size();
		}

		// second scan
		cur_add = 0;
		for (int cur_module = 0; cur_module < num_m; cur_module++){
			Module module = all_module.get(cur_module);
			ArrayList<Integer> text_list = module.get_text_list();
			// iterate through current module text
			for(int j = 0; j < text_list.size(); j++){
				int cur_text = text_list.get(j);
				String err = "";
				String res = "";
				// process address
				if(cur_text % 10 == 1){
					cur_text = cur_text / 10;
					res = Integer.toString(cur_text);
				} else if(cur_text % 10 == 2){
					cur_text = cur_text / 10;
					int check_add = cur_text - (cur_text / 1000) * 1000;
					if (check_add >= MACHINESIZE){
						err = " Error: Absolute address exceeds machine size; largest legal value used.";
						cur_text = (cur_text / 1000) * 1000 + (MACHINESIZE - 1);
					}
					res = Integer.toString(cur_text);
				} else if (cur_text % 10 == 3){
					cur_text = cur_text / 10;
					res = Integer.toString(cur_text + cur_add);
				} else if (cur_text % 10 == 4){
					cur_text = (cur_text / 10000) * 1000;
					Var cur_var = var_table.get(module.get_use(j));
					// if var not defined
					if(cur_var == null){
						int var_add = 111;
						cur_text += var_add;
						res = Integer.toString(cur_text);
						err = " Error: " + module.get_use(j) + " is not defined; 111 used.";
					} else{
						cur_var.set_used();
						int var_add = cur_var.get_abs_add();
						cur_text += var_add;
						res = Integer.toString(cur_text);
						// if mult reference
						if(module.check_mult_ref(j) == 1){
							err = " Error: Multiple variables used in instruction; all but last ignored.";
						}
					}
				}
				// process result
				if(err == ""){
					module.add_result(res);
				} else {
					module.add_result(res + err);
				}
			}
			// go for next module
			cur_add += module.get_len();
		}

		// print result
		ArrayList<Var> var_sort = new ArrayList<>(); // sort var list
		for (Enumeration<Var> all_var = var_table.elements(); all_var.hasMoreElements();){
			Var variable = all_var.nextElement();
			var_sort.add(variable);
		}
		Collections.sort(var_sort, new Comparator<Process>(){
			@Override
			public int compare(Var one, Process two){
				String name1 = one.get_name();
				String name2 = two.get_name();
				return name1.compareTo(name2);
			}
		});
		System.out.print("Symbol Table\n");
		cur_add = 0;
		for (int i = 0; i < var_sort.size(); i++){
			Var variable = var_sort.get(i);
			String var_name = variable.get_name();
			int var_add = variable.get_abs_add();
			String res = var_name + "=" + Integer.toString(var_add);
			if(variable.get_mult_def()){
				res += " Error: This variable is multiply defined; last value used.";
			}
			if(variable.too_long(symbol_len)){
				res += " Error: The symbol is too long, the maximum number of characters in a symbol is " + Integer.toString(symbol_len) + ".";
			}
			if(variable.get_exceed_size()){
				res += " Error: Definition exceeds module size; last word in module used.";
			}
			System.out.println(res);
		}
		System.out.print("\nMemory Map\n");
		for (int i = 0; i < all_module.size(); i++){
			Module module = all_module.get(i);
			ArrayList<String> result = module.get_result();
			for (int j = 0; j < result.size(); j++){
				System.out.println(Integer.toString(cur_add) + ":\t" + result.get(j));
				cur_add ++;
			}
		}
		for(int i = 0; i < var_sort.size(); i++){
			Var variable = var_sort.get(i);
			if(!variable.get_used()){
				int mod_num = variable.get_module().get_module_num();
				System.out.print("\nWarning: " + variable.get_name() + " was defined in module " + Integer.toString(mod_num) + " but never used.");
			}
		}

	}
}