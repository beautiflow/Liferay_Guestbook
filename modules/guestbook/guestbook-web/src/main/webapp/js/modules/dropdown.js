function Dropdown (selector){
    /*
    *   Declare
    */
    this._selector = selector;

    const toggleDropdown = (event) => {
        event.stopPropagation();

        const dropdownMenu = event.currentTarget.nextElementSibling;
        dropdownMenu.classList.toggle('show');
    }

    /*
         Initialzation
    */
    this.init = async function(){
        const dropdownToggleButtons = document.querySelectorAll(this._selector);

        dropdownToggleButtons.forEach(function (button){
            button.addEventListener('click', toggleDropdown);
        });

        window.addEventListener('click', function (event){
            const target = event.target;

            dropdownToggleButtons.forEach(function (button){
                const dropdownMenu = button.nextElementSibling;

                if(!dropdownMenu.contains(target)){
                    dropdownMenu.classList.remove('show');
                }
            });
        });
    }
    this.init();
};