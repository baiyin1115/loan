@/*
    名称查询条件标签的参数说明:

    name : 查询条件的名称
    id : 查询内容的input框id
@*/
<div class="input-group">
    <div class="input-group-btn">
        <button data-toggle="dropdown" class="btn btn-white dropdown-toggle"
                type="button">${name}
        </button>
    </div>
    @/* <input type="text" class="form-control" id="${id}" placeholder="${placeholder!}" /> @*/
    <input class="form-control" id="${id}" name="${id}"
           @if(isNotEmpty(value)){
           value="${tool.dateType(value)}"
           @}
           @if(isNotEmpty(type)){
           type="${type}"
           @}else{
           type="text"
           @}
           @if(isNotEmpty(readonly)){
           readonly="${readonly}"
           @}
           @if(isNotEmpty(clickFun)){
           onclick="${clickFun}"
           @}
           @if(isNotEmpty(style)){
           style="${style}"
           @}
           @if(isNotEmpty(disabled)){
           disabled="${disabled}"
           @}
           @if(isNotEmpty(changeFun)){
           onchange="${changeFun}"
           @}
    >
    @if(isNotEmpty(hidden)){
    <input class="form-control" type="hidden" id="${hidden}" value="${hiddenValue!}">
    @}
    @if(isNotEmpty(selectFlag)){
    <div id="${selectId}" style="display: none; position: absolute; z-index: 200;">
        <ul id="${selectTreeId}" class="ztree tree-box" style="${selectStyle!}"></ul>
    </div>
    @}

</div>