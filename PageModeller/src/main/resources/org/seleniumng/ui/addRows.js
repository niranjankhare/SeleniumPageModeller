function add_NewRow(){
add_Row(false);
}

var propertyMap = null;

function add_Row(f){
	var table = document.getElementById('propsview');
	var rowCount = table.getElementsByTagName('tbody')[0].rows.length;
	var row =  (table.getElementsByTagName('tbody')[0]).insertRow(-1);
	var rowId = 'Row' + rowCount;
	row.id = rowId;
	row.setAttribute ('style', 'visibility:inherit;');
	var oper = document.getElementById('oper');
	
	var resp = Promise.resolve(getTableFields('propsview'));
	resp.then(function (dbColumns){
		var index = dbColumns.indexOf('CONTROLNAME');
		
		for (var i = 0; i < dbColumns.length; i++) {
			var cellContent = null;
			if (i === dbColumns.indexOf('STANDARDCLASS')) {
				cellContent = document.createElement('select');
				getStandardtypes(cellContent);
			}
			else {
				
				if (i< index){
					cellContent = document.createElement('input');
					if (!f){
						cellContent.setAttribute ('type', 'hidden');
						cellContent.disabled=true;
					}
				} else
					cellContent = document.createElement('textarea');
			} 
			cellContent.placeholder = dbColumns[i];
			cellContent.name = rowId + '.' + dbColumns[i];
			cellContent.id = cellContent.name;
			cellContent.style.resize = 'none';
			var cell = row.insertCell(-1);
			cell.appendChild(cellContent);
		}
		var popupBtn = document.createElement('button');
		popupBtn.type = 'button';
		popupBtn.setAttribute ('onclick','showMoreProps(this, "'+ oper.value +'")');
		popupBtn.id = rowId + '.popupBtn';
		popupBtn.appendChild(document.createTextNode("Define More\nProperties"));
		popupBtn.style.resize = 'none';
		popupBtn.setAttribute ('rowid', rowId);
		var cellButton = row.insertCell(-1);
		cellButton.appendChild(popupBtn);
		var cellPopupDiv = row.insertCell(-1);
		cellPopupDiv.setAttribute ('style', 'visibility:hidden;');
		var popupDiv = document.createElement('div');
		popupDiv.id = rowId +'.popupDiv';
		popupDiv.setAttribute ('style', 'visibility:hidden;display:block');
		popupDiv.setAttribute ('rowid', rowId);
		cellPopupDiv.appendChild(popupDiv);
	});

}


function add_UpdateRow(){
	var tname = 'propsview';
	var pageName = document.getElementById('pageName').value;
	var elTable = document.getElementById(tname);
	
	var respFields = Promise.resolve(getTableData(tname, pageName));
	var oper = document.getElementById('oper').value;
	respFields.then(function (tableData){
		var headerRow = document.getElementById('headerRow');
		var dbColumns = tableData[0];
		var indexToHide = dbColumns.indexOf('CONTROLNAME');
		var selectIndex = dbColumns.indexOf('STANDARDCLASS');
		for (var i = 0; i < dbColumns.length; i++) {
			var th = document.createElement('th');
			if (i< indexToHide){
				th.setAttribute ('style', 'display:none;');
			}
            headerRow.appendChild(th).innerHTML=dbColumns[i];
        }
		headerRow.appendChild(document.createElement('th')).innerHTML='More properties';
		
		for (var k = 1; k < tableData.length; k++) {
				
				var rowData = tableData[k];
				var rowIdmain = 'Row' + (k-1);
				var row = (elTable.getElementsByTagName('tbody')[0]).insertRow(-1);
				row.id = rowIdmain;
				for (var r=0; r<rowData.length; r++){
					var rowId = rowIdmain + '.' + dbColumns[r];
					var cell = row.insertCell(-1);
					var hiddenInput = document.createElement('input');
					if (r< indexToHide){
						cell.setAttribute ('style', 'display:none;');
					} 
					hiddenInput.setAttribute ('name', rowId);
					hiddenInput.id = hiddenInput.name;
					hiddenInput.value = rowData[r];
					cell.appendChild(hiddenInput);
				}
				var popupBtn = document.createElement('button'); 
				popupBtn.type = 'button'; 
				popupBtn.setAttribute
					  ('onclick','showMoreProps(this, "' + oper + '")');
				popupBtn.id = rowIdmain +
					  '.popupBtn'; 
				popupBtn.appendChild(document.createTextNode("Define More\nProperties")); 
			  popupBtn.style.resize = 'none';
			  popupBtn.setAttribute ('rowid', rowIdmain); 
			  var cellButton = row.insertCell(-1); 
			  cellButton.appendChild(popupBtn); 
			  var cellPopupDiv = row.insertCell(-1); 
			  cellPopupDiv.setAttribute ('style', 'visibility:hidden;'); 
			  var popupDiv = document.createElement('div'); 
			  popupDiv.id = rowIdmain +'.popupDiv';
			  popupDiv.setAttribute ('style', 'visibility:hidden;display:block'); 
			  popupDiv.setAttribute ('rowid', rowIdmain); 
			  cellPopupDiv.appendChild(popupDiv); 
				
			}
			
			  
			 
		});
	}

function getStandardtypes(e){
	Promise.resolve(getData('/fetch/libdatabase/getstandardypes'))
	.then(function(resp){
		getSelectControlt(resp, e);
	})
	.catch (function(error){
		return null;
	});
}

async function getData(u){
	let response = await fetch(u);
	return response.json();
}
function getTableFields(tname){
	r = Promise.resolve(getData('/fetch/libdatabase/gettablefields?tableName='+tname)); 
	return r;
}

function getTableData(tname,pname){
	r = Promise.resolve(getData('/fetch/libdatabase/gettabledata?tableName='+tname+"&pageName="+ pname)); 
	return r;
}

function getExtendedPageGuiData(tname,guiId){
	r = Promise.resolve(getData('/fetch/libdatabase/pageguiextendedprops?tableName='+tname+"&guiId="+ guiId)); 
	return r;
}

function getSelectControl (options){
	var opt = document.createElement('option');
	for (var j = 0; j < options.length; j++) {
		opt.text = options[j][1];
		opt.value = options[j][0];
	} 
	
	return opt;
}

function getSelectControlt (options, element){
	for (var key in options){
		var opt = document.createElement('option');
		opt.text = options[key][0];
		opt.value = key;
		element.appendChild(opt);
	}
}

function showMoreProps(e , op){
	e.disabled = true;
	var rowId = e.getAttribute('rowid');
	var popup = document.getElementById(rowId+'.popupDiv');
	if (!popup.contains(popup.querySelector('div[id=\'popupTitle\']')))
		fillPopup_new (popup, op);
	popup.style.visibility = 'visible';
	popup.style.display = 'inline';
	showRowById(rowId);
	disableMainForm(op);		
}

function showRowById (id){
	var row = document.getElementById(id);
	row.style.visibility = 'visible';

}

function disableMainForm (op){
	disableMainFormButtions(op);
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'hidden';
}

function disableMainFormButtions(op){
	if (op === 'new'){
		document.getElementById('addRow').disabled = true;
	}
	document.getElementById('submit').disabled = true;
}

function getPopupCloseButtonForRowId (rowId, op) {
	var divCloseX = document.createElement('button');
	divCloseX.type = 'button';
	divCloseX.setAttribute ('rowid', rowId);
	divCloseX.setAttribute ('style','background: none; border:none; color:red; cursor:pointer;');
	divCloseX.setAttribute ('onclick','closeMoreProps(this, "'+op+'")');
	divCloseX.appendChild(document.createTextNode("X"));
	return divCloseX;
}

function closeMoreProps(e, op){
	var id = e.getAttribute('rowid');
	var idPopup = id+'.popupDiv';
	document.getElementById(id+'.popupBtn').disabled = false;
	enableMainForm(op);
	resetRowById(id);
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'inherit';
	var row = document.getElementById(id);
	var popup = document.getElementById(idPopup);
	row.style.visibility = 'inherit';
	popup.style.visibility = 'hidden';
	popup.style.display = 'none';
}

function enableMainForm (oper){
	enableMainFormButtions(oper);
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'inherit';
}
function enableMainFormButtions(op){
	
	if (op === 'new'){
		document.getElementById('addRow').disabled = false;
	}
	document.getElementById('submit').disabled = false;
}

function resetRowById (id){
	var row = document.getElementById(id);
	row.style.visibility = 'inherit';

}

function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
}

function addRowToPopup(popup,rowContent){
	var rowId = popup.getAttribute('rowId');
	var tableId = rowId+'.exPropsTable';
	console.log(tableId);
	var pTable = document.getElementById(tableId);
	var existingRowCount = pTable.rows.length;
	var contentIndex = 0;
	var displayCell;
	var displayText;
	var cellContent;
	var valueCell ;
	for (var i in rowContent){
		var row;
		displayText = rowContent[i];
		if (contentIndex>(existingRowCount-1)){
			row = pTable.insertRow(-1);	
			displayCell = row.insertCell(-1);
			cellContent = document.createElement('textarea');
			
			
			valueCell =	row.insertCell(-1);
			
		}	
		else {
			row =pTable.rows[contentIndex];
			displayCell = row.cells[0];
			valueCell = row.cells[1];
			cellContent = valueCell.getElementsByTagName("textarea")[0];
		}
		row.id = rowId;
		displayCell.innerHTML =displayText;
		cellContent.name = rowId + '.' + i;
		cellContent.id = cellContent.name;	
		cellContent.placeholder = displayText;
		valueCell.innerHTML = cellContent.outerHTML;
		contentIndex++;
	}
	for (var d=0; d<(existingRowCount-contentIndex);d++){
		pTable.deleteRow(contentIndex);
	}
	console.log ('was: existingRowCount:' + existingRowCount);
	console.log ('contentIndex:' + contentIndex);
	console.log ('new:'+ pTable.rows.length);
	
	
}

function refreshPopup (popup, val){
	var keys = JSON.parse(propertyMap[val][1]);
	var rowId= popup.getAttribute('rowId');
	
	for (var i=0; i < 9; i++){
		var placeholder = 'EXPROP'+(i+1);
		var currentRow = document.getElementById(rowId+'.POPUP.'+ placeholder);
		
		if (keys[placeholder] == null){
			currentRow.disabled=true;
			currentRow.style.visibility = 'hidden';
			currentRow.style.display = 'none';
		} else{
			var labelCell = document.getElementById(rowId + '.' + placeholder+'.label');
			labelCell.innerHTML = keys[placeholder];
			var valueCell = document.getElementById(rowId + '.' + placeholder);
			valueCell.placeholder = labelCell.innerHTML;
			currentRow.disabled=false;
			currentRow.style.visibility = 'inherit';
			currentRow.style.display = 'block';
		}
	}
	
}
	
function getOptionId (el, mapClass){
	var i = 0;
	for (var o of el.options){
		if (o.text === mapClass){
			i = o.index;
			break;
		}
		 
		}
	return i;
}
	
	
function fillPopup_new(p, op){
	var rowId = p.getAttribute('rowId');
	var title = document.createElement ('div');
	title.setAttribute('id', 'popupTitle');
	title.appendChild(document.createTextNode("Define More\nProperties"));
	title.appendChild (getPopupCloseButtonForRowId(rowId, op));
	p.appendChild(title);
	var content = document.createElement('div');
	content.setAttribute ('id', rowId+'.contentDiv');
	content.appendChild(document.createTextNode("Extend to class:"));
	var sel = document.createElement('select');
	p.appendChild(content);
	sel.name = rowId + '.MAPPEDCLASS';
	sel.setAttribute('rowId',rowId);
	sel.setAttribute ('onchange','refreshPopup(document.getElementById("'+p.id +'"), this.value)');
	content.appendChild(sel);
	
	
	var guimapId = document.getElementById(rowId+ '.GUIMAPID').value;
	var tableName = 'EXTENDEDPROPSVIEW';
	var pageName = document.getElementById('pageName').value;
	
	Promise.resolve(getData('/fetch/libdatabase/getextendedproptypes'))
	.then(function(resp){
		if (propertyMap ===null){
			propertyMap = resp;
		}
		getSelectControlt(resp, sel);
		
		addExtendedPropsTableToPopup (p, op);
		p.appendChild(content);
		
		if (op==='update'){
			Promise.resolve(getExtendedPageGuiData(tableName, guimapId))
			 .then(function(resp){
				console.log (resp);
				var mapClass = document.getElementById(rowId+'.MAPPEDCLASS').value;
				sel.value = mapClass;
				refreshPopup (p,sel.value);
				var keys = JSON.parse(propertyMap[mapClass][1]);
				var flds = resp[0];
				var vals = resp[1];
				for (var key in keys){
					console.log(key);
					var valueCell = document.getElementById(rowId + '.' + key);
					valueCell.value = vals[flds.indexOf(key)];
				}
				content.style.visibility='inherit';
			})
			.catch (function(error){
				
			});
			
			}
		else {
			
			refreshPopup (p,sel.value);
			content.style.visibility='inherit';
		}
		
		
		
		
		
		
		
		
		
		
	})
	.catch (function(error){
		
	});
		
	
}

function addTableToPopupDeprecated(popup,tbit, rowContent, operation){

	var rowId = popup.getAttribute('rowId');
	var content = document.getElementById(rowId+'.contentDiv');
	var tableId = rowId+'.exPropsTable.'+ tbit ;
	var exPropsTable = document.createElement('table');
	exPropsTable.appendChild(document.createElement('tbody'));
	exPropsTable.setAttribute("id", tableId);
	content.appendChild(exPropsTable);
	console.log(tableId);
	var pTable = document.getElementById(tableId);
	var existingRowCount = pTable.rows.length;
	var contentIndex = 0;
	var displayCell;
	var displayText;
	var cellContent;
	var valueCell ;
	for (var i in rowContent){
		var row;
		displayText = rowContent[i];
		if (contentIndex>(existingRowCount-1)){
			row = pTable.insertRow(-1);	
			displayCell = row.insertCell(-1);
			cellContent = document.createElement('textarea');
			
			
			valueCell =	row.insertCell(-1);
			
		}	
		else {
			row =pTable.rows[contentIndex];
			displayCell = row.cells[0];
			valueCell = row.cells[1];
			cellContent = valueCell.getElementsByTagName("textarea")[0];
		}
		row.id = rowId;
		displayCell.innerHTML =displayText;
		cellContent.name = rowId + '.' + i;
		cellContent.id = cellContent.name;	
		cellContent.placeholder = displayText;
		valueCell.innerHTML = cellContent.outerHTML;
		contentIndex++;
	}
	exPropsTable.disabled=true;
	exPropsTable.style.visibility = 'hidden';
	exPropsTable.style.display = 'none';
	
	console.log ('was: existingRowCount:' + existingRowCount);
	console.log ('contentIndex:' + contentIndex);
	console.log ('new:'+ pTable.rows.length);
	
	
}

function addExtendedPropsTableToPopup(popup, operation){

	var rowId = popup.getAttribute('rowId');
	var content = document.getElementById(rowId+'.contentDiv');
	var tableId = rowId+'.exPropsTable';
	var exPropsTable = document.createElement('table');
	exPropsTable.appendChild(document.createElement('tbody'));
	exPropsTable.setAttribute("id", tableId);
	content.appendChild(exPropsTable);
	console.log(tableId);
	var pTable = document.getElementById(tableId);
	var existingRowCount = pTable.rows.length;
	var contentIndex = 0;
	var labelCell;
	var labelText;
	var cellContent;
	var valueCell ;
	
	for (var i=0; i < 9; i++){
		var row = pTable.insertRow(-1);	
		var placeholder = 'EXPROP'+(i+1);
		labelText = placeholder;
		labelCell = row.insertCell(-1);
		cellContent = document.createElement('textarea');
		
		
		valueCell =	row.insertCell(-1); 
		row.id = rowId+'.POPUP.'+ placeholder;
		labelCell.innerHTML =labelText;
		cellContent.name = rowId + '.'+ 'EXPROP'+(i+1);
		cellContent.id = cellContent.name;
		labelCell.id = cellContent.id+'.label';
		cellContent.placeholder = labelText;
		valueCell.innerHTML = cellContent.outerHTML;
	}
	content.style.visibility='hidden';

}


