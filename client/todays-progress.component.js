import progressInputComponent from './progress-input.component';
import './todays-progress.component.less';

module.exports.default = angular
  .module('todaysProgress', [progressInputComponent.default])
  .component('todaysProgress', {
    bindings: {},
    controller: TodaysProgress,
    template: require('./todays-progress.component.html')
  }).name;

TodaysProgress.$inject = ['$resource', '$timeout'];
function TodaysProgress($resource, $timeout) {
  let ctrl = this;
  let Books = $resource('/books/:bookName', null, {
    addBookProgress: {method: 'PUT', isArray: false}
  });

  ctrl.$onInit = () => {
    queryBooks();
    ctrl.progressUpdating = false;
  };

  function queryBooks() {
    Books.query().$promise.then(books => {
      ctrl.books = books.map(book => {
        return Books.get({bookName: book});
      });
    });
  }

  ctrl.toggleInputActiveFor = (book, shouldBeActive) => {
    if (shouldBeActive) {
      ctrl.inputsActiveOn = book;
    } else {
      ctrl.inputsActiveOn = null;
    }
  };

  ctrl.nameToHexColor = name => {
    let hash = 0;
    for (let i = 0; i < name.length; i++) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash);
    }
    let color = '#';
    for (let i = 0; i < 3; i++) {
      let value = (hash >> (i * 8)) & 0xff;
      color += `00${value.toString(16)}`.substr(-2);
    }
    let transparency10Percent = `33`; // cudos to @lopspower https://gist.github.com/lopspower/03fb1cc0ac9f32ef38f4
    return `${color}${transparency10Percent}`;
  };

  ctrl.updateBookProgress = (bookName, currentPage) => {
    if (!angular.isDefined(currentPage)) return;

    ctrl.progressUpdating = true;
    Books.addBookProgress(
      {bookName: bookName},
      {currentPage: currentPage},
      () => {
        ctrl.progressUpdating = false;
        ctrl.showRequestConfirmation = true;
        $timeout(() => (ctrl.showRequestConfirmation = false), 8000);
        queryBooks();
      }
    );
  };

  ctrl.progressInPercent = book => {
    return Math.min(
      (book.book.currentPage / book.book.pagesTotal) * 100,
      book.book.pagesTotal
    );
  };

  ctrl.onlyUnfinishedBooks = book => {
    if (!angular.isDefined(book.book)) return false;

    return book.book.currentPage < book.book.pagesTotal;
  };
}
